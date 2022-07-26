// code by vc
// inspired by https://www.mathopenref.com/coordpolygonarea.html
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.hs.r2.Det2D;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;

/** polygon not necessarily convex
 * 
 * computes signed area circumscribed by given polygon
 * area is positive when polygon is in counter-clockwise direction */
public enum PolygonArea {
  ;
  /** @param polygon
   * @return */
  public static Scalar of(Tensor polygon) {
    if (Tensors.isEmpty(polygon))
      return RealScalar.ZERO;
    Tensor prev = Last.of(polygon);
    Scalar sum = Det2D.of(prev, prev);
    for (Tensor tensor : polygon)
      sum = sum.add(Det2D.of(prev, prev = tensor));
    return sum.multiply(RationalScalar.HALF);
  }
}
