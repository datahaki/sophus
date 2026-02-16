// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.Conjugate;

public enum FrobeniusForm implements BilinearForm {
  INSTANCE;

  @Override // from BilinearForm
  public Scalar formEval(Tensor u, Tensor v) {
    return Flatten.scalars(Entrywise.mul().apply(u, v.maps(Conjugate.FUNCTION))) //
        .reduce(Scalar::add) //
        .orElseThrow();
  }

  @Override // from BilinearForm
  public Scalar norm(Tensor u) {
    return FrobeniusNorm.of(u);
  }
}
