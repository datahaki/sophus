// code by jph
package ch.alpine.sophus.math.sca;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Exp;
import ch.alpine.tensor.sca.Factorial;

/** ( Exp [ Mu ] - 1 ) / Mu
 * 
 * @see Logc */
public enum Expc implements ScalarUnaryOperator {
  FUNCTION;

  static final ScalarUnaryOperator SERIES = //
      Polynomial.of(Tensors.vector(i -> Factorial.of(i + 1).reciprocal(), 10));

  @Override
  public Scalar apply(Scalar mu) {
    return Chop._10.isZero(mu) //
        ? SERIES.apply(mu)
        : evaluate(mu);
  }

  /* package */ static Scalar evaluate(Scalar mu) {
    return Exp.FUNCTION.apply(mu).subtract(RealScalar.ONE).divide(mu);
  }
}
