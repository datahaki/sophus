// code by jph
// adapted from PNPOLY - Point Inclusion in Polygon Test W. Randolph Franklin (WRF)
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;

/* package */ enum FranklinPnpoly {
  ;
  /** @param polygon in the 2-dimensional plane
   * @param point of which only the first two coordinates will be considered
   * @return true, if point is inside polygon, otherwise false
   * @throws Exception if the first two entries of point are not of type {@link Scalar} */
  public static boolean isInside(Tensor polygon, Tensor point) {
    final Scalar tx = point.Get(0);
    final Scalar ty = point.Get(1);
    if (Tensors.isEmpty(polygon)) // TODO SOPHUS ALG should be obsolete -> use empty region
      return false;
    boolean c = false;
    Tensor prev = Last.of(polygon);
    for (Tensor next : polygon) {
      Scalar py = prev.Get(1);
      Scalar ny = next.Get(1);
      if (Scalars.lessThan(ty, ny) != Scalars.lessThan(ty, py)) {
        Scalar div = py.subtract(ny);
        // assume div == 0 => py == ny => IF-condition above is false; therefore here div != 0
        Scalar px = prev.Get(0);
        Scalar nx = next.Get(0);
        Scalar r1 = px.subtract(nx).multiply(ty.subtract(ny));
        c ^= Scalars.lessThan(tx, r1.divide(div).add(nx));
      }
      prev = next;
    }
    return c;
  }
}
