// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.Hypot;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.pow.Power;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;

/** generalization of complex power (x+y*I)^n in two
 * dimensions {x, y}^n to three dimensions {x, y, z}^n
 * 
 * see Mandelbulb fractal */
public enum NylanderPower {
  ;
  /** @param vector of length 3 with entries {x, y, z}
   * @param exponent
   * @return */
  public static Tensor of(Tensor vector, Scalar exponent) {
    if (vector.length() != 3)
      throw new Throw(vector, exponent);
    Scalar x = vector.Get(0);
    Scalar y = vector.Get(1);
    Scalar z = vector.Get(2);
    Scalar phi = ArcTan.of(x, y).multiply(exponent);
    Scalar theta = ArcTan.of(z, Hypot.of(x, y)).multiply(exponent);
    Scalar sin_theta = Sin.FUNCTION.apply(theta);
    Scalar r = Vector2Norm.of(vector);
    ScalarUnaryOperator power = Power.function(exponent);
    return Tensors.of( //
        sin_theta.multiply(Cos.FUNCTION.apply(phi)), //
        sin_theta.multiply(Sin.FUNCTION.apply(phi)), //
        Cos.FUNCTION.apply(theta)).multiply(power.apply(r));
  }

  /** @param vector of length 3 with entries {x, y, z}
   * @param exponent
   * @return */
  public static Tensor of(Tensor vector, Number exponent) {
    return of(vector, RealScalar.of(exponent));
  }
}
