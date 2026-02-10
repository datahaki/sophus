// code by jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class LinearBiinvariantMeanTest {
  @Test
  void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(3));
    int length = 10;
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, length));
    Tensor mean = LinearBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._14.requireClose(mean, weights.dot(sequence));
  }

  @Test
  void testExact() {
    Distribution distribution = DiscreteUniformDistribution.of(10, 100);
    int length = 10;
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, length));
    Tensor mean = LinearBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._14.requireClose(mean, weights.dot(sequence));
    ExactTensorQ.require(mean);
  }

  @Test
  void testEmpty() {
    assertThrows(Exception.class, () -> LinearBiinvariantMean.INSTANCE.mean(Tensors.empty(), Tensors.empty()));
  }

  @Test
  void testSimple2() {
    Tensor sequence = Tensors.of( //
        RGroup.INSTANCE.exponential0().exp(Tensors.vector(+1 + 0.3, 0, 0)), //
        RGroup.INSTANCE.exponential0().exp(Tensors.vector(+0 + 0.3, 0, 0)), //
        RGroup.INSTANCE.exponential0().exp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = MeanDefect.of( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), //
        RGroup.INSTANCE.exponential(RGroup.INSTANCE.exponential0().exp(Tensors.vector(+0.3, 0, 0)))).tangent();
    Chop._10.requireAllZero(log);
  }
}
