// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.crv.ArcTan2D;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.so2.AngleVector;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RpnBiinvariantMeanTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final BarycentricCoordinate[] PROJECTED_COORDINATES = GbcHelper.barycentrics(RpnManifold.INSTANCE);
  private static final MeanDefect MEAN_DEFECT = BiinvariantMeanDefect.of(RpnManifold.INSTANCE);

  public void testSpecific() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (BarycentricCoordinate barycentricCoordinate : PROJECTED_COORDINATES)
      for (int count = 0; count < 10; ++count) {
        Tensor rotation = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
        Tensor mean = rotation.dot(NORMALIZE.apply(Tensors.vector(1, 1, 1)));
        Tensor sequence = Tensor.of(IdentityMatrix.of(3).stream().map(rotation::dot));
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, 1)));
        Chop._12.requireAllZero(MEAN_DEFECT.defect(sequence, weights, mean));
        {
          Tensor point = RpnBiinvariantMean.of(Chop._06).mean(sequence, weights);
          Chop._05.requireAllZero(MEAN_DEFECT.defect(sequence, weights, point));
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
          Tensor weights = ConstantArray.of(RationalScalar.of(1, n), n);
          Tensor point = RpnBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._12.requireClose(ArcTan2D.of(point), Mean.of(angles));
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 7);
  }
}
