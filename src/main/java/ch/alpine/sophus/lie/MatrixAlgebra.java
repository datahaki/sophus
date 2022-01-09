// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.MatrixBracket;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LeastSquares;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.sca.N;

public class MatrixAlgebra implements LieAlgebra, Serializable {
  private final Tensor basis;
  private final Tensor m;
  private final Tensor ad;

  public MatrixAlgebra(Tensor basis) {
    this.basis = ExactTensorQ.require(basis);
    int n = basis.length();
    m = Transpose.of(Tensor.of(basis.stream().map(Flatten::of)));
    ad = Transpose.of(Tensors.matrix((i, j) -> exact(MatrixBracket.of(basis.get(i), basis.get(j))), n, n), 2, 1, 0);
  }

  private Tensor exact(Tensor rhs) {
    Tensor b = Flatten.of(rhs);
    Tensor x = LinearSolve.any(m, b);
    if (m.dot(x).equals(b))
      return x;
    throw TensorRuntimeException.of(rhs);
  }

  @Override
  public Tensor ad() {
    return ad;
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    // TODO determine nilpotency -> then can do exact
    return BakerCampbellHausdorff.of(N.DOUBLE.of(ad), degree);
  }

  /** @param matrix linear combination of basis
   * @return */
  public Tensor toVector(Tensor matrix) {
    Tensor rhs = Flatten.of(matrix);
    Tensor x = LeastSquares.of(m, rhs);
    Tolerance.CHOP.requireClose(m.dot(x), rhs);
    return x;
  }

  /** @param vector
   * @return */
  public Tensor toMatrix(Tensor vector) {
    return vector.dot(basis);
  }
}
