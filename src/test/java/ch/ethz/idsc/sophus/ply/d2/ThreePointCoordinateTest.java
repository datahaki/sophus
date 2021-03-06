// code by jph
package ch.ethz.idsc.sophus.ply.d2;

import java.io.IOException;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.sample.BoxRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class ThreePointCoordinateTest extends TestCase {
  private static BarycentricCoordinate r2(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return HsCoordinates.wrap(RnManifold.INSTANCE, ThreePointCoordinate.of(biFunction));
  }

  public void testHDual() {
    BarycentricCoordinate barycentricCoordinate = r2(Barycenter.WACHSPRESS);
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor weights = barycentricCoordinate.weights(P, Tensors.vector(4, 2));
    Tensor exp = Tensors.fromString("{4/11, 2, 2/3, 4/33}");
    Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(exp));
  }

  public void testWeights() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(r2(Barycenter.WACHSPRESS));
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor x = Tensors.vector(4, 2);
    Tensor weights = barycentricCoordinate.weights(P, x);
    Tensor exp = Tensors.fromString("{3/26, 33/52, 11/52, 1/26}");
    Chop._12.requireClose(weights, exp);
    Chop._10.requireClose(weights.dot(P), x);
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(r2(Barycenter.DISCRETE_HARMONIC));
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").map(s -> Quantity.of(s, "m"));
    Tensor x = Tensors.vector(4, 2).map(s -> Quantity.of(s, "m"));
    Tensor weights = barycentricCoordinate.weights(P, x);
    Tensor exp = Tensors.vector(0.120229008, 0.629770992, 0.230916031, 0.019083969);
    Chop._08.requireClose(weights, exp);
    Chop._10.requireClose(weights.dot(P), x);
  }

  public void testQuantity2() throws ClassNotFoundException, IOException {
    for (Barycenter barycentric : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = Serialization.copy(r2(barycentric));
      Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").map(s -> Quantity.of(s, "m"));
      Tensor x = Tensors.vector(4, 2).map(s -> Quantity.of(s, "m"));
      Tensor weights = barycentricCoordinate.weights(P, x);
      Chop._10.requireClose(weights.dot(P), x);
    }
  }

  public void testScalingInvariant() {
    Scalar factor = RealScalar.of(2.3);
    Tensor polygon1 = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor polygon2 = polygon1.multiply(factor);
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(Tensors.vector(0, 0), Tensors.vector(5, 5));
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      // TensorUnaryOperator function1 = powerCoordinates.weights(polygon1);
      // TensorUnaryOperator function2 = powerCoordinates.weights(polygon2);
      for (int count = 0; count < 10; ++count) {
        Tensor point = RandomSample.of(randomSampleInterface);
        if (Polygons.isInside(polygon1, point)) {
          Tensor w1 = barycentricCoordinate.weights(polygon1, point);
          Tensor w2 = barycentricCoordinate.weights(polygon2, point.multiply(factor));
          Chop._08.requireClose(w1, w2);
          Chop._10.requireClose(w1.dot(polygon1), point);
          Chop._10.requireClose(w2.dot(polygon2), point.multiply(factor));
        }
      }
    }
  }

  public void testLagrangeProperty() {
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").unmodifiable();
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      // TensorUnaryOperator function = powerCoordinates.of(P);
      for (int index = 0; index < P.length(); ++index) {
        Tensor x = P.get(index);
        Tensor weights = barycentricCoordinate.weights(P, x);
        assertEquals(weights, UnitVector.of(4, index));
        Chop._10.requireClose(weights.dot(P), x);
      }
    }
  }

  public void testEdges() {
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").unmodifiable();
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      for (int index = 0; index < P.length(); ++index) {
        // Tensor weights =
        barycentricCoordinate.weights(P, Mean.of(RotateLeft.of(P, index).extract(0, 2)));
        // System.out.println(weights);
        // weights.stream().map(Scalar.class::cast).forEach(Sign::requirePositiveOrZero);
      }
    }
  }

  public void testEdgesTriangle() {
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {4, 4}}").unmodifiable();
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      for (int index = 0; index < P.length(); ++index) {
        Tensor x = Mean.of(RotateLeft.of(P, index).extract(0, 2));
        Tensor weights = barycentricCoordinate.weights(P, x);
        weights.stream().map(Scalar.class::cast).forEach(Sign::requirePositiveOrZero);
        // Chop._10.requireClose(weights.dot(P), x);
      }
    }
  }

  public void testNonPlanarFail() {
    Distribution distribution = UniformDistribution.unit();
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      AssertFail.of(() -> barycentricCoordinate.weights(RandomVariate.of(distribution, 10, 3), Tensors.vector(1, 1, 1)));
    }
  }

  public void testFailEmpty() {
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      AssertFail.of(() -> barycentricCoordinate.weights(Tensors.empty(), Tensors.empty()));
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> ThreePointCoordinateTest.r2(null));
  }
}
