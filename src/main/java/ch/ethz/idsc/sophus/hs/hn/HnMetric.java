// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ArcCosh;
import ch.ethz.idsc.tensor.sca.Chop;

/** hyperboloid model
 * 
 * Reference:
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum HnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    return ArcCosh.FUNCTION.apply(cosh_d(x, y));
  }

  /** @param x
   * @param y
   * @return result guaranteed to be greater equals 1 */
  public static Scalar cosh_d(Tensor x, Tensor y) {
    StaticHelper.requirePoint(x);
    StaticHelper.requirePoint(y);
    Scalar xy = HnBilinearForm.between(x, y).negate();
    if (Scalars.lessEquals(RealScalar.ONE, xy))
      return xy;
    // TODO use taylor series
    Chop._08.requireClose(xy, RealScalar.ONE);
    return RealScalar.ONE;
  }
}
