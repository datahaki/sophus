// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.itp.LanczosInterpolation;

/** implementation is specific to R^n */
public enum LanczosCurve {
  ;
  /** @param points
   * @param number strictly positive
   * @return */
  public static Tensor refine(Tensor points, int number) {
    return Subdivide.of(0, points.length() - 1, number).map(LanczosInterpolation.of(points)::at);
  }
}
