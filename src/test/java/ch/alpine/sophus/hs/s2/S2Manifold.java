// code by jph
package ch.alpine.sophus.hs.s2;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.sophus.hs.sn.SnTransport;
import ch.alpine.tensor.Tensor;

/** 2-dimensional tangent space parameterization */
public enum S2Manifold implements HsManifold {
  INSTANCE;

  @Override // from HsManifold
  public Exponential exponential(Tensor p) {
    return new S2Exponential(p);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor p) {
    return new S2Exponential(p);
  }

  @Override
  public HsTransport hsTransport() {
    return SnTransport.INSTANCE;
  }
}
