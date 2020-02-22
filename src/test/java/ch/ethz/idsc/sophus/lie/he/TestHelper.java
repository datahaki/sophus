// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Clips;

/* package */ enum TestHelper {
  ;
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(10));

  static Tensor spawn_random(int n) {
    return Tensors.of( //
        RandomVariate.of(DISTRIBUTION, n), //
        RandomVariate.of(DISTRIBUTION, n), //
        RandomVariate.of(DISTRIBUTION)); // element
  }
}
