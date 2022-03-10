// code by jph
package ch.alpine.sophus.lie.dt;

import java.io.IOException;
import java.util.Random;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class DtManifoldTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(DtGroup.INSTANCE);
  private static final BarycentricCoordinate AFFINE = AffineWrap.of(DtManifold.INSTANCE);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      // LeveragesCoordinate.slow(DtManifold.INSTANCE, InversePowerVariogram.of(1)), //
      // LeveragesCoordinate.slow(DtManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE };

  public void testSimple() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          int fn = n;
          RandomSampleInterface rsi = new DtRandomSample(fn, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
          Tensor sequence = RandomSample.of(rsi, random, length);
          Tensor mean1 = RandomSample.of(rsi, random);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._06.requireClose(mean1, mean2);
          // ---
          Tensor shift = RandomSample.of(rsi, random);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._03.requireClose(weights, //
                barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  public void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          barycentricCoordinate = Serialization.copy(barycentricCoordinate);
          RandomSampleInterface rsi = new DtRandomSample(n, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
          Tensor sequence = RandomSample.of(rsi, random, length);
          Tensor mean1 = RandomSample.of(rsi, random);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = DtBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(mean1, mean2); // linear reproduction
          // ---
          Tensor shift = RandomSample.of(rsi, random);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._05.requireClose(weights, barycentricCoordinate.weights( //
                tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  public void testAffineCenter() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = n + 2; length < n + 8; ++length) {
        RandomSampleInterface rsi = new DtRandomSample(n, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
        Tensor sequence = RandomSample.of(rsi, length);
        Tensor constant = AveragingWeights.of(length);
        Tensor center = DtBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }
}
