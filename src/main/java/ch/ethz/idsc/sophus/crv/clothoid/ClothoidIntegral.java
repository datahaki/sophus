// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

public class ClothoidIntegral implements Serializable {
  /** The integral of exp i*clothoidQuadratic as suggested in U. Reif slides has the
   * property, that the values of the polynomial correspond to the tangent angle.
   * 
   * @param lagrangeQuadratic
   * @return */
  public static ClothoidIntegral interp(LagrangeQuadratic lagrangeQuadratic) {
    return new ClothoidIntegral(PartialInterface.of(lagrangeQuadratic.c0, lagrangeQuadratic.c1, lagrangeQuadratic.c2));
  }

  /***************************************************/
  private final PartialInterface partialInterface;
  private final Scalar one;

  private ClothoidIntegral(PartialInterface partialInterface) {
    this.partialInterface = partialInterface;
    one = partialInterface.il(RealScalar.ONE);
  }

  /** interpolation of terminal points
   * t == 0 -> (0, 0)
   * t == 1 -> (1, 0) */
  public Scalar normalized(Scalar t) {
    return partialInterface.il(t).divide(one);
  }

  /** a return value with Im[one] == 0 implies that {@link #normalized(Scalar)}
   * does not distort tangents at end points t == 0, and t == 1
   * 
   * @return approximate integration of exp i*clothoidQuadratic on [0, 1] */
  public Scalar one() {
    return one;
  }
}
