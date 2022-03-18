// code by jph
package ch.alpine.sophus.lie.sc;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.red.GeometricMean;
import ch.alpine.tensor.sca.Chop;

public class ScBiinvariantMeanTest {
  @Test
  public void testSimple() {
    Tensor scalar = ScBiinvariantMean.INSTANCE.mean(Tensors.vector(1, 2, 3).map(Tensors::of), Tensors.fromString("{1/3, 1/3, 1/3}"));
    Chop._10.requireClose(scalar, Tensors.vector(1.8171205928321397));
  }

  @Test
  public void testGeometricMean() {
    Distribution distribution = ExponentialDistribution.of(1);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n);
      Scalar geomet = (Scalar) GeometricMean.of(sequence);
      Tensor weights = AveragingWeights.of(sequence.length());
      Tensor scmean = ScBiinvariantMean.INSTANCE.mean(sequence.map(Tensors::of), weights);
      Chop._10.requireClose(Tensors.of(geomet), scmean);
    }
  }
}
