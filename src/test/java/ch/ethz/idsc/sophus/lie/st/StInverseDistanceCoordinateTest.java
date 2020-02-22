// code by jph
package ch.ethz.idsc.sophus.lie.st;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Round;
import junit.framework.TestCase;

public class StInverseDistanceCoordinateTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);

  public void testSimple() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(StInverseDistanceCoordinate.INSTANCE);
    for (int n = 1; n < 3; ++n)
      for (int length = n + 2; length < n + 3; ++length) {
        int fn = n;
        System.out.println("HERE");
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
        Tensor mean1 = TestHelper.spawn_St(n);
        System.out.println("NORMAL");
        Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
        Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights1);
        Chop._10.requireClose(mean1, mean2);
        // ---
        Tensor shift = TestHelper.spawn_St(n);
        StGroupElement lieGroupElement = StGroup.INSTANCE.element(shift);
        // invariant under left action
        System.out.println("LEFT");
        Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.left(sequence, shift), lieGroupElement.combine(mean1));
        Chop._10.requireClose(weights1, weightsL);
        System.out.println("WL=" + weightsL.map(Round._3));
        // invariant under right action
        System.out.println("RIGHT");
        Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.right(sequence, shift), StGroup.INSTANCE.element(mean1).combine(shift));
        System.out.println("WR=" + weightsR.map(Round._3));
        Chop._10.requireClose(weights1, weightsR);
        // invariant under inversion
        System.out.println("INVERSION");
        Tensor weightsI = barycentricCoordinate.weights( //
            LIE_GROUP_OPS.invertAll(sequence), //
            StGroup.INSTANCE.element(mean1).inverse().toCoordinate());
        // Chop._06.requireClose(weights1, weightsI);
      }
  }
}
