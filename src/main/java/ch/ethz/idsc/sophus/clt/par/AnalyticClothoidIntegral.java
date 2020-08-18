// code by ureif
package ch.ethz.idsc.sophus.clt.par;

import java.io.Serializable;

import ch.ethz.idsc.sophus.clt.LagrangeQuadratic;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

public class AnalyticClothoidIntegral implements ClothoidIntegral, Serializable {
  /** The integral of exp i*clothoidQuadratic as suggested in U. Reif slides has the
   * property, that the values of the polynomial correspond to the tangent angle.
   * 
   * @param lagrangeQuadratic
   * @return */
  public static ClothoidIntegral interp(LagrangeQuadratic lagrangeQuadratic) {
    return of(AnalyticPartial.of(lagrangeQuadratic));
  }

  /** @param partialInterface
   * @return */
  public static ClothoidIntegral of(PartialInterface partialInterface) {
    return new AnalyticClothoidIntegral(partialInterface);
  }

  /***************************************************/
  private final PartialInterface partialInterface;
  private final Scalar one;

  private AnalyticClothoidIntegral(PartialInterface partialInterface) {
    this.partialInterface = partialInterface;
    one = partialInterface.il(RealScalar.ONE);
  }

  @Override
  public Scalar normalized(Scalar t) {
    return partialInterface.il(t).divide(one);
  }

  @Override
  public Scalar one() {
    return one;
  }
}
