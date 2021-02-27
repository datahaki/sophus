// code by jph
package ch.ethz.idsc.sophus.math.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.num.Series;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Factorial;

/** ( Exp [ Mu ] - 1 ) / Mu
 * 
 * @see Logc */
public enum Expc implements ScalarUnaryOperator {
  FUNCTION;

  static final ScalarUnaryOperator SERIES = //
      Series.of(Tensors.vector(i -> Factorial.of(i + 1).reciprocal(), 10));

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
