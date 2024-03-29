// code by jph
package ch.alpine.sophus.hs.rpn;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.dv.AveragingWeights;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;

class RpnManifoldTest {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = GbcHelper.barycentrics(RpnManifold.INSTANCE);

  @Test
  void testSpecific() {
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
          Tensor point = RpnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights);
          Chop._05.requireAllZero(new MeanDefect(sequence, weights, RpnManifold.INSTANCE.exponential(point)).tangent());
        }
      }
  }

  @Test
  void testRp1Linear() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(0, Math.PI / 4);
    for (int n = 2; n < 5; ++n)
      for (int count = 0; count < 5; ++count) {
        Tensor angles = RandomVariate.of(distribution, random, n);
        Tensor sequence = angles.map(AngleVector::of);
        Tensor weights = AveragingWeights.of(n);
        Tensor point = RpnManifold.INSTANCE.biinvariantMean(Chop._14).mean(sequence, weights);
        Chop._12.requireClose(ArcTan2D.of(point), Mean.of(angles));
      }
  }

  @Test
  void testDistance() {
    Tolerance.CHOP.requireZero(RpnManifold.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(+10, 0, 0)));
    Tolerance.CHOP.requireZero(RpnManifold.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(-10, 0, 0)));
  }
}
