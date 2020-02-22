// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** ( Exp [ Mu ] - 1 ) / Mu
 * 
 * @see Logc */
public enum Expc implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar mu) {
    return Scalars.isZero(mu) //
        ? RealScalar.ONE
        : Exp.FUNCTION.apply(mu).subtract(RealScalar.ONE).divide(mu);
  }
}
