// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Hermite subdivision on manifolds via parallel transport"
 * by Caroline Moosmueller, 2017 */
public enum So3Transport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor qpinv = q.dot(Inverse.of(p));
    return vp -> {
      StaticHelper.requireTangent(p, vp);
      return qpinv.dot(vp);
    };
  }
}
