// code by jph
package ch.ethz.idsc.sophus.hs.s3;

import ch.ethz.idsc.sophus.hs.sn.S3UnitQuaternionDistance;
import ch.ethz.idsc.sophus.math.Metric;
import ch.ethz.idsc.tensor.Quaternion;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Log;

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
