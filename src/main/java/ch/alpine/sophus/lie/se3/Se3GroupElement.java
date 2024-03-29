// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.mat.MatrixQ;

/** linear map that transforms tangent vector at the identity
 * to vector in tangent space of given group element
 * 
 * code based on derivation by Ethan Eade
 * "Lie Groups for 2D and 3D Transformations", p. 8 */
public class Se3GroupElement implements LieGroupElement {
  /** 3 x 3 orthogonal matrix */
  private final Tensor R;
  private final Tensor t;

  public Se3GroupElement(Tensor R, Tensor t) {
    this.R = MatrixQ.require(R);
    this.t = VectorQ.requireLength(t, 3);
  }

  /** @param g element from Lie Group SE3 as 4x4 affine matrix */
  public Se3GroupElement(Tensor g) {
    R = Se3Matrix.rotation(g);
    t = Se3Matrix.translation(g);
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return Se3Matrix.of(R, t);
  }

  @Override // from LieGroupElement
  public Se3GroupElement inverse() {
    Tensor tR = Transpose.of(R);
    return new Se3GroupElement(tR, tR.dot(t).negate());
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor g) {
    Se3GroupElement eg = new Se3GroupElement(g);
    return Se3Matrix.of(R.dot(eg.R), R.dot(eg.t).add(t));
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor u_w) {
    Tensor u = u_w.extract(0, 3); // translation
    Tensor w = u_w.extract(3, 6); // rotation
    Tensor rw = R.dot(w);
    return Join.of( //
        R.dot(u).add(Cross.of(t, rw)), //
        rw);
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  public Tensor rotation() {
    return R.unmodifiable();
  }

  public Tensor translation() {
    return t.unmodifiable();
  }
}
