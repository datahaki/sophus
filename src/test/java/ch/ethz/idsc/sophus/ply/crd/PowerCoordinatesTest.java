// code by jph
package ch.ethz.idsc.sophus.ply.crd;

import java.io.IOException;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.math.sample.BoxRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class PowerCoordinatesTest extends TestCase {
  public void testGetDual() {
    PowerCoordinates powerCoordinates = new PowerCoordinates(Barycentric.WACHSPRESS);
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    // Tensor weights = powerCoordinates.getDual(P, Tensors.vector(4, 2), Tensors.vector(1, 1, 1, 1));
    // Tensor exp = Tensors.fromString("{{40/11, 23/11}, {4, 1}, {6, 3}, {4, 7/3}}");
    // System.out.println(weights);
    // Chop._12.requireClose(weights, exp);
  }

  public void testHDual() {
    PowerCoordinates powerCoordinates = new PowerCoordinates(Barycentric.WACHSPRESS);
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor weights = powerCoordinates.hDual(P, Tensors.vector(4, 2));
    Tensor exp = Tensors.fromString("{4/11, 2, 2/3, 4/33}");
    Chop._12.requireClose(weights, exp);
  }

  public void testWeights() throws ClassNotFoundException, IOException {
    PowerCoordinates powerCoordinates = Serialization.copy(new PowerCoordinates(Barycentric.WACHSPRESS));
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor weights = powerCoordinates.weights(P, Tensors.vector(4, 2));
    Tensor exp = Tensors.fromString("{3/26, 33/52, 11/52, 1/26}");
    Chop._12.requireClose(weights, exp);
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    PowerCoordinates powerCoordinates = Serialization.copy(new PowerCoordinates(Barycentric.DISCRETE_HARMONIC));
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").map(s -> Quantity.of(s, "m"));
    Tensor weights = powerCoordinates.weights(P, Tensors.vector(4, 2).map(s -> Quantity.of(s, "m")));
    Tensor exp = Tensors.vector(0.120229008, 0.629770992, 0.230916031, 0.019083969);
    Chop._08.requireClose(weights, exp);
  }

  public void testQuantity2() throws ClassNotFoundException, IOException {
    for (Barycentric barycentric : Barycentric.values()) {
      PowerCoordinates powerCoordinates = Serialization.copy(new PowerCoordinates(barycentric));
      Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").map(s -> Quantity.of(s, "m"));
      powerCoordinates.weights(P, Tensors.vector(4, 2).map(s -> Quantity.of(s, "m")));
    }
  }

  public void testScalingInvariant() {
    Scalar factor = RealScalar.of(2.3);
    Tensor polygon1 = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}");
    Tensor polygon2 = polygon1.multiply(factor);
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(Tensors.vector(0, 0), Tensors.vector(5, 5));
    for (BiFunction<Tensor, Scalar, Tensor> barycentric : Barycentric.values()) {
      PowerCoordinates powerCoordinates = new PowerCoordinates(barycentric);
      for (int count = 0; count < 10; ++count) {
        Tensor point = RandomSample.of(randomSampleInterface);
        Tensor w1 = powerCoordinates.weights(polygon1, point);
        Tensor w2 = powerCoordinates.weights(polygon2, point.multiply(factor));
        Chop._08.requireClose(w1, w2);
      }
    }
  }

  public void testCorners() {
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").unmodifiable();
    for (Barycentric barycentric : Barycentric.values()) {
      PowerCoordinates powerCoordinates = new PowerCoordinates(barycentric);
      for (int index = 0; index < P.length(); ++index) {
        Tensor weights = powerCoordinates.weights(P, P.get(index));
        assertEquals(weights, UnitVector.of(4, index));
      }
    }
  }

  public void testEdges() {
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {3, 5}, {2, 5}}").unmodifiable();
    for (Barycentric barycentric : Barycentric.values()) {
      PowerCoordinates powerCoordinates = new PowerCoordinates(barycentric);
      for (int index = 0; index < P.length(); ++index) {
        Tensor weights = powerCoordinates.weights(P, Mean.of(RotateLeft.of(P, index).extract(0, 2)));
        weights.stream().map(Scalar.class::cast).forEach(Sign::requirePositiveOrZero);
      }
    }
  }

  public void testEdgesTriangle() {
    Tensor P = Tensors.fromString("{{1, 1}, {5, 1}, {4, 4}}").unmodifiable();
    for (Barycentric barycentric : Barycentric.values()) {
      PowerCoordinates powerCoordinates = new PowerCoordinates(barycentric);
      for (int index = 0; index < P.length(); ++index) {
        Tensor weights = powerCoordinates.weights(P, Mean.of(RotateLeft.of(P, index).extract(0, 2)));
        weights.stream().map(Scalar.class::cast).forEach(Sign::requirePositiveOrZero);
      }
    }
  }

  public void testFailEmpty() throws ClassNotFoundException, IOException {
    PowerCoordinates powerCoordinates = Serialization.copy(new PowerCoordinates(Barycentric.MEAN_VALUE));
    // Tensor weights = powerCoordinates.weights(Tensors.empty(), Tensors.vector(4, 2));
    // assertEquals(weights, Tensors.empty());
    try {
      // fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
