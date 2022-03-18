// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public enum HnManifold implements HsManifold {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor x) {
    return new HnExponential(x);
  }

  @Override // from HsManifold
  public Exponential exponential(Tensor p) {
    return new HnExponential(p);
  }

  @Override
  public Tensor flip(Tensor p, Tensor q) {
    Scalar nxy = LBilinearForm.between(p, q).negate();
    return p.add(p).multiply(nxy).subtract(q);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return HnProjection.INSTANCE.apply(p.add(q));
  }
}
