// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface BiinvariantVectorFunction {
  BiinvariantVector biinvariantVector(Tensor point);
}
