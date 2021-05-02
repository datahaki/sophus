// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface BiinvariantVectorFunction {
  BiinvariantVector biinvariantVector(Tensor point);
}
