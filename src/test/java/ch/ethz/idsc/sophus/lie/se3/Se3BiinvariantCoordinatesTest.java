// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.BarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se3BiinvariantCoordinatesTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      HsBiinvariantCoordinate.linear(Se3Manifold.INSTANCE), //
      HsBiinvariantCoordinate.smooth(Se3Manifold.INSTANCE) };
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se3Group.INSTANCE);

  public void testRandom() {
    BiinvariantMean biinvariantMean = Se3BiinvariantMean.INSTANCE;
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 7; n < 12; ++n)
        try {
          Tensor points = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
          Tensor xya = TestHelper.spawn_Se3();
          Tensor weights1 = barycentricCoordinate.weights(points, xya);
          AffineQ.require(weights1);
          Tensor check1 = biinvariantMean.mean(points, weights1);
          Chop._10.requireClose(check1, xya);
          Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
          Tensor x_recreated = biinvariantMean.mean(points, weights1);
          Chop._06.requireClose(xya, x_recreated);
          Tensor shift = TestHelper.spawn_Se3();
          { // invariant under left action
            Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
            Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
            Tensor x_lft = biinvariantMean.mean(seqlft, weights1);
            Chop._10.requireClose(xyalft, x_lft);
            Tensor weightsL = barycentricCoordinate.weights(seqlft, xyalft);
            Chop._10.requireClose(weights1, weightsL);
          }
          { // invariant under right action
            Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
            Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
            Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
            Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
            Chop._10.requireClose(xyargt, x_rgt);
            Chop._10.requireClose(weights1, weightsR);
          }
          { // invariant under inversion
            Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
            Tensor xyainv = LIE_GROUP_OPS.invert(xya);
            Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
            Tensor check2 = biinvariantMean.mean(seqinv, weightsI);
            Chop._10.requireClose(check2, xyainv);
            AffineQ.require(weightsI);
            Chop._10.requireClose(weights1, weightsI);
          }
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 3);
  }
}
