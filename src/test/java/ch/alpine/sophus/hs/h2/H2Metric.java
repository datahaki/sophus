// code by ob
package ch.alpine.sophus.hs.h2;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.sca.ArcCosh;
import ch.alpine.tensor.sca.Sign;

/** Careful: H2Metric uses as coordinates the Klein model
 * whereas HnMetric uses as coordinates the hyperboloid model!
 * 
 * Source:
 * https://en.wikipedia.org/wiki/Poincar%C3%A9_half-plane_model#Distance_calculation */
public enum H2Metric implements TensorMetric {
  INSTANCE;

  /** @param p element in H2 of the form {px, (0 <) py}
   * @param q element in H2 of the form {qx, (0 <) qy}
   * @return length of geodesic between p and q
   * @throws Exception if the y component of p or q is not strictly positive */
  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Scalar pqy = Sign.requirePositive(p.Get(1)).multiply(Sign.requirePositive(q.Get(1)));
    return ArcCosh.FUNCTION.apply(Vector2NormSquared.between(p, q).divide(pqy.add(pqy)).add(RealScalar.ONE));
  }
}