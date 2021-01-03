// code by jph
package ch.ethz.idsc.sophus.lie.dt;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.ExponentialDistribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION_L = ExponentialDistribution.of(1);
  private static final Distribution DISTRIBUTION_T = NormalDistribution.standard();

  static Tensor spawn_St(int n) {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_L), RandomVariate.of(DISTRIBUTION_T, n));
  }

  static Tensor spawn_st(int n) {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_T), RandomVariate.of(DISTRIBUTION_T, n));
  }

  static Tensor spawn_St1() {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_L), RandomVariate.of(DISTRIBUTION_T));
  }

  static Tensor spawn_st1() {
    return Tensors.of(RandomVariate.of(DISTRIBUTION_T), RandomVariate.of(DISTRIBUTION_T));
  }
}
