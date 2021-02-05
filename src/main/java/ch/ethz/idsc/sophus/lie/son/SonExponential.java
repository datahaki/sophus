// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.lie.MatrixGroupExponential;
import ch.ethz.idsc.sophus.math.Vectorize;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;

/** SO(n) */
public class SonExponential extends MatrixGroupExponential {
  private final Tensor pinv;

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
    return SonMemberQ.INSTANCE.require(p);
  }

  @Override
  protected Tensor requireTangent(Tensor vp) {
    return new TSonMemberQ(p()).require(vp);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return Vectorize.of(log(q), -1);
  }
}
