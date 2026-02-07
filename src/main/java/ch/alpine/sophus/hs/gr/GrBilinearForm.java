// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.MatrixDotConjugateTranspose;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Re;

/* package */ enum GrBilinearForm implements BilinearForm {
  INSTANCE;

  @Override
  public Scalar formEval(Tensor u, Tensor v) {
    return Re.FUNCTION.apply(Trace.of(MatrixDotConjugateTranspose.of(u, v)));
  }
}
