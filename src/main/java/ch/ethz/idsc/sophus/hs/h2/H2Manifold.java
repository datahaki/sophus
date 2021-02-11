// code by jph
package ch.ethz.idsc.sophus.hs.h2;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;

public enum H2Manifold implements VectorLogManifold {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor p) {
    return new H2TangentSpace(p);
  }
}
