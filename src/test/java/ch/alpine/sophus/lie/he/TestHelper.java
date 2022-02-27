// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(10));

  static Tensor spawn_He(int n) {
    return spawn_he(n);
  }

  static Tensor spawn_he(int n) {
    return Tensors.of( //
        RandomVariate.of(DISTRIBUTION, n), //
        RandomVariate.of(DISTRIBUTION, n), //
        RandomVariate.of(DISTRIBUTION)); // element
  }
}
