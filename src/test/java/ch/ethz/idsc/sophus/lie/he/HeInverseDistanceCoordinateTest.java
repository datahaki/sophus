// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HeInverseDistanceCoordinateTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      HeInverseDistanceCoordinate.INSTANCE, //
      HeInverseDistanceCoordinate.BIINVAR };
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);

  public void testSimple() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
          int fn = n;
          // System.out.println("HERE");
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_He(fn), length);
          Tensor mean1 = TestHelper.spawn_He(n);
          Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights1);
          Chop._08.requireClose(mean1, mean2);
          // ---
          Tensor shift = TestHelper.spawn_He(n);
          { // invariant under left action
            Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.allL(sequence, shift), LIE_GROUP_OPS.combine(shift, mean1));
            Chop._10.requireClose(weights1, weightsL);
            // System.out.println("WL=" + weightsL.map(Round._3));
          }
          { // invariant under right action
            Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allR(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
            // System.out.println("WR=" + weightsR.map(Round._3));
            Chop._10.requireClose(weights1, weightsR);
          }
          { // invariant under inversion
            Tensor weightsI = barycentricCoordinate.weights( //
                LIE_GROUP_OPS.allI(sequence), //
                LIE_GROUP_OPS.invert(mean1));
            // System.out.println("WI=" + weightsI.map(Round._3));
            Chop._10.requireClose(weights1, weightsI);
          }
        }
  }
}
