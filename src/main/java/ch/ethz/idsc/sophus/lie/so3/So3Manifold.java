// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public enum So3Manifold implements HsExponential, VectorLogManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor point) {
    return new So3Exponential(point);
  }

  @Override // from FlattenLogManifold
  public TangentSpace logAt(Tensor point) {
    return new So3Exponential(point);
  }
}
