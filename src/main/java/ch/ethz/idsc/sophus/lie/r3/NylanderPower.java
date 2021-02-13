// code by jph
package ch.ethz.idsc.sophus.lie.r3;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.nrm.Hypot;
import ch.ethz.idsc.tensor.nrm.VectorNorm2Squared;
import ch.ethz.idsc.tensor.sca.ArcTan;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.Sin;

/** generalization of complex power (x+y*I)^n in two
 * dimensions {x, y}^n to three dimensions {x, y, z}^n
 * 
 * see Mandelbulb fractal */
public enum NylanderPower {
  ;
  private static final Scalar HALF = RealScalar.of(0.5);

  /** @param vector of length 3 with entries {x, y, z}
   * @param exponent
   * @return */
  public static Tensor of(Tensor vector, Scalar exponent) {
    if (vector.length() != 3)
      throw TensorRuntimeException.of(vector, exponent);
    Scalar x = vector.Get(0);
    Scalar y = vector.Get(1);
    Scalar z = vector.Get(2);
    Scalar phi = ArcTan.of(x, y).multiply(exponent);
    Scalar theta = ArcTan.of(z, Hypot.of(x, y)).multiply(exponent);
    Scalar sin_theta = Sin.FUNCTION.apply(theta);
    Scalar r = VectorNorm2Squared.of(vector);
    return Tensors.of( //
        sin_theta.multiply(Cos.FUNCTION.apply(phi)), //
        sin_theta.multiply(Sin.FUNCTION.apply(phi)), //
        Cos.FUNCTION.apply(theta)).multiply(Power.of(r, exponent.multiply(HALF)));
  }

  /** @param vector of length 3 with entries {x, y, z}
   * @param exponent
   * @return */
  public static Tensor of(Tensor vector, Number exponent) {
    return of(vector, RealScalar.of(exponent));
  }
}
