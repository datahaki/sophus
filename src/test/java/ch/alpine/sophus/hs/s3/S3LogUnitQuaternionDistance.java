// code by jph
package ch.alpine.sophus.hs.s3;

import ch.alpine.sophus.hs.sn.S3UnitQuaternionDistance;
import ch.alpine.sophus.math.Metric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.lie.Quaternion;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Log;

/** implementation for evaluation or {@link S3UnitQuaternionDistance}
 * 
 * <p>Reference:
 * https://en.wikipedia.org/wiki/Quaternion */
/* package */ enum S3LogUnitQuaternionDistance implements Metric<Quaternion> {
  INSTANCE;

  @Override // from Metric
  public Scalar distance(Quaternion p, Quaternion q) {
    return Abs.FUNCTION.apply(Log.FUNCTION.apply(p.reciprocal().multiply(q)));
  }
}
