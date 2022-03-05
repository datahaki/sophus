// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnBiinvariantMeanTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = GbcHelper.barycentrics(SnManifold.INSTANCE);

  public void testSpecific() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      Tensor rotation = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Tensor mean = rotation.dot(Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 1)));
      Tensor sequence = Tensor.of(IdentityMatrix.of(3).stream().map(rotation::dot));
      Chop._08.requireClose(sequence, Transpose.of(rotation));
      Tensor weights = barycentricCoordinate.weights(sequence, mean);
      Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, 1)));
      Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
      Chop._12.requireAllZero(evaluate);
      Chop._05.requireClose(mean, SnBiinvariantMean.of(Chop._06).mean(sequence, weights));
    }
  }

  public void testS1Linear() {
    Distribution distribution = UniformDistribution.of(0, Math.PI);
    for (int n = 2; n < 10; ++n) {
      Tensor angles = RandomVariate.of(distribution, n);
      Tensor sequence = angles.map(AngleVector::of);
      Tensor weights = AveragingWeights.of(n);
      Tensor point = SnBiinvariantMean.of(Chop._06).mean(sequence, weights);
      Chop._05.requireClose(ArcTan2D.of(point), Mean.of(angles));
    }
  }
}
