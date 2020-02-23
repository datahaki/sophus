// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class StInverseDistanceCoordinateTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      // StInverseDistanceCoordinate.INSTANCE, //
      // StInverseDistanceCoordinate.SQUARED, //
      StAffineCoordinate.INSTANCE };

  public void testSimple() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          int fn = n;
          // System.out.println("HERE "+barycentricCoordinate.getClass().getSimpleName());
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
          Tensor mean1 = TestHelper.spawn_St(n);
          // System.out.println("NORMAL");
          Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights1);
          Tolerance.CHOP.requireClose(mean1, mean2);
          // ---
          Tensor shift = TestHelper.spawn_St(n);
          // invariant under left action
          {
            Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.allL(sequence, shift), LIE_GROUP_OPS.combine(shift, mean1));
            Chop._08.requireClose(weights1, weightsL);
            // System.out.println("WL=" + weightsL.map(Round._3));
          }
          // invariant under right action
          {
            Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allR(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
            // System.out.println("WR=" + weightsR.map(Round._3));
            Chop._10.requireClose(weights1, weightsR);
          }
          // invariant under inversion
          {
            Tensor weightsI = barycentricCoordinate.weights( //
                LIE_GROUP_OPS.allI(sequence), LIE_GROUP_OPS.invert(mean1));
            // System.out.println("WI=" + weightsI.map(Round._3));
            Chop._06.requireClose(weights1, weightsI);
          }
        }
  }
}
