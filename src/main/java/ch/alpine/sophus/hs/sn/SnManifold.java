// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;

public enum SnManifold implements HsManifold {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new SnExponential(point);
  }

  @Override // from HsManifold
  public Exponential exponential(Tensor point) {
    return new SnExponential(point);
  }

  @Override // from HsManifold
  public Tensor flip(Tensor p, Tensor q) {
    Tensor r = p.multiply((Scalar) p.dot(q));
    return r.add(r).subtract(q);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return Vector2Norm.NORMALIZE.apply(p.add(q));
  }
}
