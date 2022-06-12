// code by gjoel
package ch.alpine.sophus.crv;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;

public record TransitionWrap(Tensor samples, Tensor spacing) implements Serializable {
  public TransitionWrap(Tensor samples, Tensor spacing) {
    this.samples = samples.unmodifiable();
    this.spacing = VectorQ.requireLength(spacing.unmodifiable(), samples.length());
  }
}
