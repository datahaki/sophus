// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Sign;

public enum Amplifiers {
  ;
  /** Properties:
   * strictly positive
   * monotonous
   * smooth
   * 0 maps to 1
   * 
   * @param sigma
   * @return */
  public static ScalarUnaryOperator exp(Scalar sigma) {
    Sign.requirePositive(sigma);
    return scalar -> Exp.FUNCTION.apply(scalar.multiply(sigma));
  }

  public static ScalarUnaryOperator exp(Number sigma) {
    return exp(RealScalar.of(sigma));
  }

  public static ScalarUnaryOperator ramp(Scalar sigma) {
    return new SmoothRamp(sigma);
  }

  public static ScalarUnaryOperator ramp(Number sigma) {
    return ramp(RealScalar.of(sigma));
  }

  public static ScalarUnaryOperator arctan(Scalar sigma) {
    return new ArcTanAmplifier(sigma);
  }

  public static ScalarUnaryOperator arctan(Number sigma) {
    return arctan(RealScalar.of(sigma));
  }
}
