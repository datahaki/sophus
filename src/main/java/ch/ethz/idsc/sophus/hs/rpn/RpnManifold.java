// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public enum RpnManifold implements HsExponential, VectorLogManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor point) {
    return new RpnExponential(point);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new RpnExponential(point);
  }
}
