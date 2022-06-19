// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.math.api.Metric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.lie.Quaternion;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.tri.ArcCos;

/** distance between two quaternions of unit length
 * 
 * <p>Reference:
 * https://en.wikipedia.org/wiki/Quaternion */
public enum S3UnitQuaternionDistance implements Metric<Quaternion> {
  INSTANCE;

  private static final Scalar HALF = RealScalar.of(0.5);

  @Override // from Metric
  public Scalar distance(Quaternion p, Quaternion q) {
    Scalar dot = p.w().multiply(q.w()).add(p.xyz().dot(q.xyz()));
    Scalar dot2 = dot.multiply(dot);
    Scalar ratio = dot2.add(dot2).subtract(RealScalar.ONE);
    if (ratio instanceof RealScalar) // similar to VectorAngle
      ratio = Clips.absoluteOne().apply(ratio);
    return ArcCos.FUNCTION.apply(ratio).multiply(HALF);
  }
}
