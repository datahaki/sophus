// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.BinningMethod;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.7)
 * in NR, 2007
 * 
 * @see BinningMethod */
public class ThinPlateSplineNorm implements TensorNorm, Serializable {
  /** @param r0 positive */
  public static TensorNorm of(Scalar r0) {
    return new ThinPlateSplineNorm(Sign.requirePositive(r0));
  }

  /***************************************************/
  private final Scalar r0;

  private ThinPlateSplineNorm(Scalar r0) {
    this.r0 = r0;
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor vector) {
    Scalar r2 = Norm2Squared.ofVector(vector);
    return Scalars.isZero(r2) //
        ? r2
        : r2.multiply(Log.FUNCTION.apply(Sqrt.FUNCTION.apply(r2).divide(r0)));
  }
}
