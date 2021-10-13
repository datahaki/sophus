// code by ob
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.math.Geodesic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.26, Section 4.1, Eq. (14).
 * 
 * Another reference for ST is "Deep Compositing Using Lie Algebras" by Tom Duff. The article
 * parameterizes the group differently with the scaling coefficient alpha := 1 - lambda */
public enum DtGeodesic implements Geodesic {
  INSTANCE;

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    DtGroupElement p_act = DtGroup.INSTANCE.element(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = DtExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(DtExponential.INSTANCE.exp(x.multiply(scalar)));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
