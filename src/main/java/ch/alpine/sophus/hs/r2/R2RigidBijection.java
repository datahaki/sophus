// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.api.Bijection;
import ch.alpine.tensor.Tensor;

/** every rigid transformation is a bijective mapping */
public interface R2RigidBijection extends Bijection {
  /** @return 3x3 matrix of rigid forward transformation at given scalar parameter */
  Tensor forward_se2();
}
