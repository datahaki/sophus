// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** (Exp [ Mu ] - 1 ) / Mu
 * 
 * @see Logc */
public enum Expc implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar t) {
    return Scalars.isZero(t) //
        ? RealScalar.ONE
        : Exp.FUNCTION.apply(t).subtract(RealScalar.ONE).divide(t);
  }
}
