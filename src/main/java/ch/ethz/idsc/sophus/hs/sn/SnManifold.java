// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

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
    return VectorNorm2.NORMALIZE.apply(p.add(q));
  }
}
