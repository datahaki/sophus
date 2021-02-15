// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** 2-dimensional tangent space parameterization */
public enum S2Manifold implements HsManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor p) {
    return new S2Exponential(p);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor p) {
    return new S2Exponential(p);
  }
}
