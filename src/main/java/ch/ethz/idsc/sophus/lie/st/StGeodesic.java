// code by ob
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;

/** "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.26, Section 4.1, Eq. (14).
 * 
 * Another reference for ST is "Deep Compositing Using Lie Algebras" by Tom Duff. The article
 * parameterizes the group differently with the scaling coefficient alpha := 1 - lambda */
public enum StGeodesic implements GeodesicInterface {
  INSTANCE;
  // ---
  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    StGroupElement p_act = new StGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = StExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(StExponential.INSTANCE.exp(x.multiply(scalar)));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
