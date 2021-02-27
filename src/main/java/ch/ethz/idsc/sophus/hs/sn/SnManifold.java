// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

public enum SnManifold implements HsManifold {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new SnExponential(point);
  }

  @Override // from HsManifold
  public Exponential exponential(Tensor point) {
    return new SnExponential(point);
  }

  @Override // from HsManifold
  public Tensor flip(Tensor p, Tensor q) {
    Tensor r = p.multiply((Scalar) p.dot(q));
    return r.add(r).subtract(q);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return Vector2Norm.NORMALIZE.apply(p.add(q));
  }

  /** function establishes orthogonal rotation matrix that rotates vector a onto b:
   * <pre>
   * SnRotationMatrix.of(a, b).dot(a) == b
   * </pre>
   * 
   * @param p vector with 2-norm equals to 1
   * @param q vector with 2-norm equals to 1
   * @return orthogonal matrix with determinant +1 (only for valid input a and b) with matrix . a == b
   * @throws Exception if either a or b are not of Euclidean length 1 */
  @SuppressWarnings("static-method")
  public Tensor endomorphism(Tensor p, Tensor q) {
    // Tensor w = TensorWedge.of(p, q).multiply(RealScalar.of(-2));
    // TODO Eade suggests to treat the case q ~= -p separately!
    Tensor pq = TensorProduct.of( //
        SnMemberQ.INSTANCE.require(p), //
        SnMemberQ.INSTANCE.require(q));
    Tensor w = Transpose.of(pq).subtract(pq);
    Scalar c = RealScalar.ONE.add(p.dot(q)).reciprocal();
    return IdentityMatrix.of(p.length()).add(w).add(w.dot(w).multiply(c));
  }
}
