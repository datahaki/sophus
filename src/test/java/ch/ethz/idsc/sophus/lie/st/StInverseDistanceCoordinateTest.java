// code by jph
package ch.ethz.idsc.sophus.lie.st;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.LieGroupTests;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class StInverseDistanceCoordinateTest extends TestCase {
  static Tensor transform(Tensor sequence, LieGroupElement lieGroupElement) {
    return Tensor.of(sequence.stream().map(lieGroupElement::combine));
  }

  public void testSimple() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(StInverseDistanceCoordinate.INSTANCE);
    for (int n = 2; n < 6; ++n)
      for (int length = n + 2; length < 12; ++length) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
        Tensor mean1 = TestHelper.spawn_St(n);
        Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
        Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights1);
        Chop._10.requireClose(mean1, mean2);
        // invariant under inversion
        Tensor weights2 = barycentricCoordinate.weights( //
            LieGroupTests.invert(StGroup.INSTANCE, sequence), //
            StGroup.INSTANCE.element(mean1).inverse().toCoordinate());
        Chop._06.requireClose(weights1, weights2);
        // ---
        StGroupElement lieGroupElement = StGroup.INSTANCE.element(TestHelper.spawn_St(n));
        Tensor weights3 = barycentricCoordinate.weights(transform(sequence, lieGroupElement), lieGroupElement.combine(mean1));
        // Chop._10.requireClose(weights1, weights3);
      }
  }
}
