// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;

// TODO this class is somewhat obsolete/redundant with LieExponential
@Deprecated
public abstract class MatrixGroupExponential implements Exponential, TangentSpace, Serializable {
  private final Tensor p;

  public MatrixGroupExponential(Tensor p) {
    this.p = p;
  }

  protected final Tensor p() {
    return p;
  }

  protected abstract Tensor p_inv();

  protected abstract Tensor requirePoint(Tensor p);

  protected abstract Tensor requireTangent(Tensor vp);

  @Override // from Exponential
  public final Tensor exp(Tensor vp) { // tangent vector at p
    return p.dot(MatrixExp.of(p_inv().dot(requireTangent(vp))));
  }

  @Override // from Exponential
  public final Tensor log(Tensor q) {
    return p.dot(MatrixLog.of(p_inv().dot(requirePoint(q))));
  }
}
