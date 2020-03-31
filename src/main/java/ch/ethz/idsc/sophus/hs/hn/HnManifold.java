// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public enum HnManifold implements HsExponential, FlattenLogManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor x) {
    return new HnExponential(x);
  }

  @Override // from FlattenLogManifold
  public FlattenLog logAt(Tensor x) {
    return new HnExponential(x);
  }
}
