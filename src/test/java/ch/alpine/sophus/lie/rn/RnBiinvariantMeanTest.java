// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class RnBiinvariantMeanTest {
  @Test
  void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(3));
    int length = 10;
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, length));
    Tensor mean = RnBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._14.requireClose(mean, weights.dot(sequence));
  }

  @Test
  void testExact() {
    Distribution distribution = DiscreteUniformDistribution.of(10, 100);
    int length = 10;
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, length));
    Tensor mean = RnBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._14.requireClose(mean, weights.dot(sequence));
    ExactTensorQ.require(mean);
  }

  @Test
  void testEmpty() {
    assertThrows(Exception.class, () -> RnBiinvariantMean.INSTANCE.mean(Tensors.empty(), Tensors.empty()));
  }

  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.argmax(RnGroup.INSTANCE, Chop._12);

  @Test
  void testSimple2() {
    Tensor sequence = Tensors.of( //
        RnGroup.INSTANCE.exp(Tensors.vector(+1 + 0.3, 0, 0)), //
        RnGroup.INSTANCE.exp(Tensors.vector(+0 + 0.3, 0, 0)), //
        RnGroup.INSTANCE.exp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = new MeanDefect( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), //
        RnGroup.INSTANCE.exponential(RnGroup.INSTANCE.exp(Tensors.vector(+0.3, 0, 0)))).tangent();
    Chop._10.requireAllZero(log);
  }

  @Test
  void testConvergence() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    final int d = 3;
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnGroup.INSTANCE))
      for (int n = d + 1; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(RnGroup.INSTANCE::exp));
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
        Optional<Tensor> optional = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, weights);
        Tensor mean = optional.get();
        Tensor w2 = barycentricCoordinate.weights(sequence, mean);
        Optional<Tensor> o2 = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, w2);
        Chop._08.requireClose(mean, o2.get());
      }
  }

  @Test
  void testConvergenceExact() {
    Random random = new Random(3);
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    int n = 4;
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnGroup.INSTANCE)) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, 3).stream().map(RnGroup.INSTANCE::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), random, n));
      Optional<Tensor> optional = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, weights);
      Tensor mean = optional.get();
      Tensor w2 = barycentricCoordinate.weights(sequence, mean);
      Optional<Tensor> o2 = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, w2);
      Chop._08.requireClose(mean, o2.get());
      Chop._08.requireClose(weights, w2);
    }
  }
}
