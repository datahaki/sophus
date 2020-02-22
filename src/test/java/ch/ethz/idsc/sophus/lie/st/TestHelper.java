// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Exp;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION = NormalDistribution.standard();

  static Tensor spawn_St(int n) {
    return Tensors.of(Exp.FUNCTION.apply(RandomVariate.of(DISTRIBUTION)), RandomVariate.of(DISTRIBUTION, n));
  }

  static Tensor spawn_st(int n) {
    return Tensors.of(RandomVariate.of(DISTRIBUTION), RandomVariate.of(DISTRIBUTION, n));
  }
}
