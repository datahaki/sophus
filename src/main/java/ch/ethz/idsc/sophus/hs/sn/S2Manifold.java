// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;

public enum S2Manifold implements VectorLogManifold {
  INSTANCE;

  @Override
  public TangentSpace logAt(Tensor xyz) {
    return new S2TangentSpace(xyz);
  }
}
