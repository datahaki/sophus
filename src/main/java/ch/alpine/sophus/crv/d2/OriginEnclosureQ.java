// code by jph
// adapted from PNPOLY - Point Inclusion in Polygon Test W. Randolph Franklin (WRF)
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.lie.r2.ConvexHull;
import ch.alpine.tensor.sca.Sign;

// TODO SOPHUS API generally, the files in this package currently dont all belong in this package together...
public enum OriginEnclosureQ implements MemberQ {
  INSTANCE;

  /** @param levers matrix of dimension n x 2
   * @return true if origin (0, 0) is inside polygon spanned by levers */
  @Override
  public boolean test(Tensor levers) {
    if (Tensors.isEmpty(levers))
      return false;
    boolean c = false;
    Tensor prev = Last.of(levers);
    for (Tensor _n : levers) {
      Tensor next = VectorQ.requireLength(_n, 2);
      Scalar py = prev.Get(1);
      Scalar ny = next.Get(1);
      if (Sign.isPositive(ny) != Sign.isPositive(py)) {
        Scalar div = py.subtract(ny);
        // assume div == 0 => py == ny => IF-condition above is false; therefore here div != 0
        Scalar px = prev.Get(0);
        Scalar nx = next.Get(0);
        Scalar r1 = nx.subtract(px).multiply(ny);
        c ^= Sign.isPositive(r1.divide(div).add(nx));
      }
      prev = next;
    }
    return c;
  }

  /** @param levers
   * @return whether origin is inside convex span of given levers */
  public static boolean isInsideConvexHull(Tensor levers) {
    return INSTANCE.test(ConvexHull.of(levers));
  }
}
