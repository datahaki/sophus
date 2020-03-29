// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.Tensor;

public enum SpdManifold implements FlattenLogManifold {
  INSTANCE;

  @Override // from FlattenLogManifold
  public FlattenLog logAt(Tensor point) {
    return new SpdExponential(point);
  }
}
