// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION_T = NormalDistribution.of(4, 1);
  private static final Distribution DISTRIBUTION_R = NormalDistribution.of(0, 0.1);

  static Tensor spawn_se3() {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_T, 3), RandomVariate.of(DISTRIBUTION_R, 3));
  }

  static Tensor spawn_Se3() {
    return Se3Exponential.INSTANCE.exp(spawn_se3());
  }
}
