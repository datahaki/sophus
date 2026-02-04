// code by jph
package ch.alpine.sophus.lie.so2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.LinearBiinvariantMean;
import ch.alpine.sophus.math.Permute;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class So2CoveringBiinvariantMeanTest {
  @Test
  void testPermutations() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.HALF));
    Distribution shifted = UniformDistribution.of(Clips.absolute(10));
    for (int length = 1; length < 6; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length).map(RealScalar.of(10)::add);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), length));
      Scalar solution = (Scalar) LinearBiinvariantMean.INSTANCE.mean(sequence, weights);
      for (int count = 0; count < 10; ++count) {
        Scalar shift = RandomVariate.of(shifted);
        for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
          TensorUnaryOperator permute = Permute.of(perm);
          Scalar result = (Scalar) LinearBiinvariantMean.INSTANCE.mean( //
              permute.apply(sequence.map(shift::add)), //
              permute.apply(weights));
          Chop._12.requireClose(result.subtract(shift), solution);
        }
      }
    }
  }

  @Test
  void testSpecific() {
    Scalar scalar = (Scalar) LinearBiinvariantMean.INSTANCE.mean(Tensors.vector(3, 4), Tensors.vector(0.5, 0.5));
    Chop._12.requireClose(scalar, RealScalar.of(3.5));
  }

  @Test
  void testAntipodal() {
    Scalar mean = (Scalar) LinearBiinvariantMean.INSTANCE.mean(Tensors.of(Pi.HALF, Pi.HALF.negate()), Tensors.vector(0.6, 0.4));
    Chop._12.requireClose(mean, RealScalar.of(0.3141592653589793));
  }

  @Test
  void testFailFar() {
    Scalar mean = (Scalar) LinearBiinvariantMean.INSTANCE.mean(Tensors.of(Pi.VALUE, Pi.VALUE.negate()), Tensors.vector(0.6, 0.4));
    Chop._12.requireClose(mean, RealScalar.of(0.6283185307179586));
  }
}
