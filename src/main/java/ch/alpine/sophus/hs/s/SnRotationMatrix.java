// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;

public enum SnRotationMatrix {
  ;
  /** function establishes orthogonal rotation matrix that rotates vector a onto b:
   * <pre>
   * SnRotationMatrix.of(p, q).dot(p) == q
   * </pre>
   * 
   * @param p vector with 2-norm equals to 1
   * @param q vector with 2-norm equals to 1
   * @return orthogonal matrix with determinant +1
   * @throws Exception if either p or q are not of Euclidean length 1 */
  public static Tensor of(Tensor p, Tensor q) {
    MemberQ memberQ = SnManifold.INSTANCE.isPointQ();
    if (Tolerance.CHOP.isClose(p, q.negate()))
      throw new Throw(p, q);
    Tensor pq = TensorProduct.of(memberQ.require(p), memberQ.require(q));
    Tensor w = Transpose.of(pq).subtract(pq); // == TensorWedge[pq] * -2
    Scalar c = RealScalar.ONE.add(p.dot(q)).reciprocal();
    return IdentityMatrix.of(p.length()).add(w).add(w.dot(w).multiply(c));
  }
}
