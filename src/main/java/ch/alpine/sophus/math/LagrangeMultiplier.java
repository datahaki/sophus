// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.mat.CholeskyDecomposition;
import ch.alpine.tensor.mat.ConjugateTranspose;
import ch.alpine.tensor.mat.LeastSquares;

/** Solves the following linear system
 * matrix=
 * [eye eqs^t]
 * [eqs 0]
 * b=[target;rhs]
 * 
 * @see ArrayFlatten */
public final class LagrangeMultiplier implements Serializable {
  private static final long serialVersionUID = -867790329410465764L;
  // ---
  private final int n;
  private final Tensor matrix;
  private final Tensor b;

  /** @param eye hermite matrix of dimensions n x n
   * @param target vector of length n
   * @param eqs matrix of dimensions d x n
   * @param rhs vector of length d
   * @throws Exception if dimensions of input parameters do not match */
  public LagrangeMultiplier(Tensor eye, Tensor target, Tensor eqs, Tensor rhs) {
    n = eye.length();
    if (target.length() != n)
      throw TensorRuntimeException.of(eye, target);
    int d = eqs.length();
    if (rhs.length() != d)
      throw TensorRuntimeException.of(eqs, rhs);
    matrix = ArrayFlatten.of(new Tensor[][] { //
        { eye, ConjugateTranspose.of(eqs) }, //
        { eqs, Array.zeros(d, d) } });
    b = Join.of(target, rhs);
  }

  /** @return */
  public Tensor matrix() {
    return matrix;
  }

  /** @return */
  public Tensor b() {
    return b;
  }

  /** @return vector x of length n that satisfies eqs.x == rhs and is close to eye.x ~ target */
  public Tensor solve() {
    try {
      return usingCholesky(); // typically faster than usingSvd
    } catch (Exception exception) {
      // matrix does not have full rank
    }
    return usingSvd();
  }

  /** Hint: only use function if rank of eye and eqs is maximal
   * 
   * @return vector x of length n that satisfies eqs.x == rhs and is close to eye.x ~ target
   * @throws Exception if rank of matrix is not maximal */
  public Tensor usingCholesky() {
    return Tensor.of(CholeskyDecomposition.of(matrix).solve(b).stream().limit(n));
  }

  /** robust solver in case rank of eye or eqs is not maximal
   * but slower than {@link #usingCholesky()}.
   * 
   * @return vector x of length n that satisfies eqs.x == rhs and is close to eye.x ~ target */
  public Tensor usingSvd() {
    return Tensor.of(LeastSquares.usingSvd(matrix, b).stream().limit(n));
  }
}
