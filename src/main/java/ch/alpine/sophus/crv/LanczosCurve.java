// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.itp.LanczosInterpolation;

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
