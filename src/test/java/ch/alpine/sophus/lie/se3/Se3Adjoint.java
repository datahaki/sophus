// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.re.Inverse;

/** linear map that transforms tangent vector at the identity
 * to vector in tangent space of given group element
 * 
 * code based on derivation by Ethan Eade
 * "Lie Groups for 2D and 3D Transformations", p. 11 */
/* package */ class Se3Adjoint implements TensorUnaryOperator {
  /** @param g element from Lie Group SE3 as 4x4 affine matrix
   * @return */
  public static TensorUnaryOperator forward(Tensor g) {
    return new Se3Adjoint(g);
  }

  /** @param g element from Lie Group SE3 as 4x4 affine matrix
   * @return */
  public static TensorUnaryOperator inverse(Tensor g) {
    return new Se3Adjoint(Inverse.of(g));
  }

  private final Tensor R;
  private final Tensor t;

  /* package */ Se3Adjoint(Tensor R, Tensor t) {
    this.R = MatrixQ.require(R);
    this.t = VectorQ.requireLength(t, 3);
  }

  /** @param g element from Lie Group SE3 as 4x4 affine matrix */
  private Se3Adjoint(Tensor g) {
    R = Se3Matrix.rotation(g);
    t = Se3Matrix.translation(g);
  }

  @Override
  public Tensor apply(Tensor u_w) {
    Tensor u = u_w.get(0); // translation
    Tensor w = u_w.get(1); // rotation
    Tensor rw = R.dot(w);
    return Tensors.of( //
        R.dot(u).add(Cross.of(t, rw)), //
        rw);
  }
}
