// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;

/** parallel transport of tangent vector at orig to dest point on manifold S^n */
public enum SnTransport implements HsTransport {
  INSTANCE;

  private static final HsMemberQ HS_MEMBER_Q = SnMemberQ.of(Chop._08);

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    Tensor matrix = SnRotationMatrix.of(orig, dest);
    return vector -> matrix.dot(HS_MEMBER_Q.requireTangent(orig, vector));
  }
}
