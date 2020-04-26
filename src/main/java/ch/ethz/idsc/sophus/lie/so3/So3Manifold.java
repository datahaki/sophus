// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public enum So3Manifold implements HsExponential, FlattenLogManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor point) {
    return new So3Exponential(point);
  }

  @Override // from FlattenLogManifold
  public FlattenLog logAt(Tensor point) {
    return new So3Exponential(point);
  }
}
