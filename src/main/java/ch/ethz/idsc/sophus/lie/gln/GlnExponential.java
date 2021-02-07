// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import ch.ethz.idsc.sophus.lie.MatrixGroupExponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.mat.Inverse;

// TODO whut!?
public class GlnExponential extends MatrixGroupExponential {
  private final Tensor pinv;

  public GlnExponential(Tensor p) {
    super(p);
    pinv = Inverse.of(p);
  }

  @Override // from MatrixExponential
  protected Tensor p_inv() {
    return pinv;
  }

  @Override
  protected Tensor requirePoint(Tensor p) {
    return p;
  }

  @Override
  protected Tensor requireTangent(Tensor vp) {
    return vp;
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return Flatten.of(log(q));
  }
}
