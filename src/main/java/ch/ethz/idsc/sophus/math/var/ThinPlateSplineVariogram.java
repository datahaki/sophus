// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.BinningMethod;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.7)
 * in NR, 2007
 * 
 * <p>The returned values have the unit of r0 squared.
 * For example if r0 has unit "m" then the returned unit is "m^2".
 * 
 * @see BinningMethod */
public class ThinPlateSplineVariogram implements ScalarUnaryOperator {
  /** @param r0 positive */
  public static ScalarUnaryOperator of(Scalar r0) {
    return new ThinPlateSplineVariogram(Sign.requirePositive(r0));
  }

  /** @param r0 positive
   * @return */
  public static ScalarUnaryOperator of(Number r0) {
    return of(RealScalar.of(r0));
  }

  /***************************************************/
  private final Scalar r0;

  private ThinPlateSplineVariogram(Scalar r0) {
    this.r0 = r0;
  }

  @Override
  public Scalar apply(Scalar r) {
    return Scalars.isZero(r) //
        ? r.multiply(r) // units consistent with case 0 < r
        : r.multiply(r).multiply(Log.FUNCTION.apply(r.divide(r0)));
  }
}
