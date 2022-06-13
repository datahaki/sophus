// code by jph
package ch.alpine.sophus.gbc.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.d2.PolygonRegion;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.sample.BoxRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.RotateLeft;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class ThreePointCoordinateTest {
  private static BarycentricCoordinate r2(ThreePointScaling biFunction) {
    return HsCoordinates.of(RnGroup.INSTANCE, ThreePointCoordinate.of(biFunction));
  }

  @Test
  void testHDual() {
    BarycentricCoordinate barycentricCoordinate = r2(Barycenter.WACHSPRESS);
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor weights = barycentricCoordinate.weights(P, Tensors.vector(4, 2));
    Tensor exp = Tensors.fromString("{4/11, 2, 2/3, 4/33}");
    Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(exp));
  }

  @Test
  void testWeights() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(r2(Barycenter.WACHSPRESS));
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor x = Tensors.vector(4, 2);
    Tensor weights = barycentricCoordinate.weights(P, x);
    Tensor exp = Tensors.fromString("{3/26, 33/52, 11/52, 1/26}");
    Chop._12.requireClose(weights, exp);
    Chop._10.requireClose(weights.dot(P), x);
  }

  @Test
  void testQuantity() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(r2(Barycenter.DISCRETE_HARMONIC));
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").map(s -> Quantity.of(s, "m"));
    Tensor x = Tensors.vector(4, 2).map(s -> Quantity.of(s, "m"));
    Tensor weights = barycentricCoordinate.weights(P, x);
    Tensor exp = Tensors.vector(0.120229008, 0.629770992, 0.230916031, 0.019083969);
    Chop._08.requireClose(weights, exp);
    Chop._10.requireClose(weights.dot(P), x);
  }

  @Test
  void testQuantity2() throws ClassNotFoundException, IOException {
    for (Barycenter barycentric : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = Serialization.copy(r2(barycentric));
      Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").map(s -> Quantity.of(s, "m"));
      Tensor x = Tensors.vector(4, 2).map(s -> Quantity.of(s, "m"));
      Tensor weights = barycentricCoordinate.weights(P, x);
      Chop._10.requireClose(weights.dot(P), x);
    }
  }

  @Test
  void testScalingInvariant() {
    Scalar factor = RealScalar.of(2.3);
    Tensor polygon1 = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    PolygonRegion polygonRegion = new PolygonRegion(polygon1);
    Tensor polygon2 = polygon1.multiply(factor);
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(Tensors.vector(0, 0), Tensors.vector(5, 5));
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      // TensorUnaryOperator function1 = powerCoordinates.weights(polygon1);
      // TensorUnaryOperator function2 = powerCoordinates.weights(polygon2);
      for (int count = 0; count < 10; ++count) {
        Tensor point = RandomSample.of(randomSampleInterface);
        if (polygonRegion.test(point)) {
          Tensor w1 = barycentricCoordinate.weights(polygon1, point);
          Tensor w2 = barycentricCoordinate.weights(polygon2, point.multiply(factor));
          Chop._08.requireClose(w1, w2);
          Chop._10.requireClose(w1.dot(polygon1), point);
          Chop._10.requireClose(w2.dot(polygon2), point.multiply(factor));
        }
      }
    }
  }

  @Test
  void testLagrangeProperty() {
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

  @Test
  void testEdges() {
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

  @Test
  void testEdgesTriangle() {
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

  @Test
  void testNonPlanarFail() {
    Distribution distribution = UniformDistribution.unit();
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      assertThrows(Exception.class, () -> barycentricCoordinate.weights(RandomVariate.of(distribution, 10, 3), Tensors.vector(1, 1, 1)));
    }
  }

  @Test
  void testFailEmpty() {
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = r2(barycenter);
      assertThrows(Exception.class, () -> barycentricCoordinate.weights(Tensors.empty(), Tensors.empty()));
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> ThreePointCoordinateTest.r2(null));
  }
}
