// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;

import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.ConjugateTranspose;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.MatrixDotConjugateTranspose;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Re;

/* package */ class StBilinearForm implements BilinearForm, Serializable {
  private final Tensor c;

  public StBilinearForm(Tensor p) {
    c = IdentityMatrix.inplaceAdd(ConjugateTranspose.of(p).dot(p).multiply(RationalScalar.of(-1, 2)));
    SymmetricMatrixQ.INSTANCE.require(c);
  }

  @Override
  public Scalar formEval(Tensor u, Tensor v) {
    return Re.FUNCTION.apply(Trace.of(u.dot(MatrixDotConjugateTranspose.of(c, v))));
  }
}
