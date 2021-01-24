// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.lie.MatrixGroupExponential;
import ch.ethz.idsc.sophus.math.Vectorize;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

/** SO(n) */
public class SonExponential extends MatrixGroupExponential {
  private final Tensor pinv;
  private static final HsMemberQ HS_MEMBER_Q = SonMemberQ.of(Chop._06);

  public SonExponential(Tensor p) {
    super(p);
    OrthogonalMatrixQ.require(p);
    pinv = Transpose.of(p);
  }

  @Override // from MatrixExponential
  protected Tensor p_inv() {
    return pinv;
  }

  @Override
  protected Tensor requirePoint(Tensor p) {
    return HS_MEMBER_Q.requirePoint(p);
  }

  @Override
  protected Tensor requireTangent(Tensor vp) {
    return HS_MEMBER_Q.requireTangent(p(), vp);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return Vectorize.of(log(q), -1);
  }
}
