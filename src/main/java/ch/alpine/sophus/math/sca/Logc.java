// code by jph
package ch.alpine.sophus.math.sca;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Log;
import ch.alpine.tensor.sca.Sinc;

/** Logc [Lambda] := Log [ Lambda ] / ( Lambda - 1 )
 *
 * @see Expc
 * @see Sinc */
public enum Logc implements ScalarUnaryOperator {
  FUNCTION;

  private static final ScalarUnaryOperator SERIES = //
      Polynomial.of(Tensors.vector(i -> RationalScalar.of(Integers.isEven(i) ? 1 : -1, i + 1), 10));

  @Override
  public Scalar apply(Scalar lambda) {
    Scalar den = lambda.subtract(RealScalar.ONE);
    return Chop._10.isZero(den) //
        ? SERIES.apply(den)
        : Log.FUNCTION.apply(lambda).divide(den);
  }
}
