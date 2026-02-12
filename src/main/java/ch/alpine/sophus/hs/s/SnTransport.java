// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** parallel transport of tangent vector at orig to dest point on manifold S^n */
public enum SnTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    TSnMemberQ tSnMemberQ = new TSnMemberQ(p);
    Tensor matrix = SnRotationMatrix.of(p, q);
    return vector -> matrix.dot(tSnMemberQ.require(vector));
  }
}
