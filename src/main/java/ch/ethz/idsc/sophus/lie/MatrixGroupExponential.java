// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;

public abstract class MatrixGroupExponential implements Exponential, TangentSpace, Serializable {
  private final Tensor p;

  public MatrixGroupExponential(Tensor p) {
    this.p = p;
  }

  /** TODO @throws Exception if vp is not tangent to p */
  @Override // from Exponential
  public final Tensor exp(Tensor vp) { // tangent vector at p
    return p.dot(MatrixExp.of(pinv().dot(vp)));
  }

  /** TODO @throws Exception if q is in manifold */
  @Override // from Exponential
  public final Tensor log(Tensor q) {
    return p.dot(MatrixLog.of(pinv().dot(q)));
  }

  protected abstract Tensor pinv();
}
