// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Conjugate;

public enum VectorBilinearForm implements BilinearForm {
  INSTANCE;

  @Override
  public Scalar formEval(Tensor u, Tensor v) {
    return (Scalar) u.dot(v.maps(Conjugate.FUNCTION));
  }
}
