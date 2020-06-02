// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public enum HnManifold implements HsExponential, VectorLogManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor x) {
    return new HnExponential(x);
  }

  @Override // from FlattenLogManifold
  public TangentSpace logAt(Tensor x) {
    return new HnExponential(x);
  }
}
