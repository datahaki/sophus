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
  protected Tensor pinv() {
    return pinv;
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return Vectorize.lt(log(q), -1);
  }
}
