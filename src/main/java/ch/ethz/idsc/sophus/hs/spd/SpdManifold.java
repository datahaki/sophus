// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public enum SpdManifold implements HsExponential, FlattenLogManifold {
  INSTANCE;

  @Override
  public Exponential exponentialAt(Tensor point) {
    return new SpdExponential(point);
  }

  @Override // from FlattenLogManifold
  public FlattenLog logAt(Tensor point) {
    return new SpdExponential(point);
  }
}
