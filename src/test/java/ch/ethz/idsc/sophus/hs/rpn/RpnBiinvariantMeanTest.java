// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.math.d2.ArcTan2D;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.r2.AngleVector;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RpnBiinvariantMeanTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = GbcHelper.barycentrics(RpnManifold.INSTANCE);

  public void testSpecific() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int count = 0; count < 10; ++count) {
        Tensor rotation = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
        Tensor mean = rotation.dot(Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 1)));
        Tensor sequence = Tensor.of(IdentityMatrix.of(3).stream().map(rotation::dot));
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, 1)));
        Chop._12.requireAllZero(new MeanDefect(sequence, weights, RpnManifold.INSTANCE.exponential(mean)).tangent());
        {
          Tensor point = RpnBiinvariantMean.of(Chop._06).mean(sequence, weights);
          Chop._05.requireAllZero(new MeanDefect(sequence, weights, RpnManifold.INSTANCE.exponential(point)).tangent());
        }
      }
  }

  public void testRp1Linear() {
    int fails = 0;
    Distribution distribution = UniformDistribution.of(0, Math.PI / 4);
    for (int n = 2; n < 5; ++n)
      for (int count = 0; count < 5; ++count)
        try {
          Tensor angles = RandomVariate.of(distribution, n);
          Tensor sequence = angles.map(AngleVector::of);
          Tensor weights = AveragingWeights.of(n);
          Tensor point = RpnBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._12.requireClose(ArcTan2D.of(point), Mean.of(angles));
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 7);
  }
}
