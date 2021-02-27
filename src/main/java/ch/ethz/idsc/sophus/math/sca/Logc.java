// code by jph
package ch.ethz.idsc.sophus.math.sca;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.num.Series;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.Sinc;

/** Logc [Lambda] := Log [ Lambda ] / ( Lambda - 1 )
 *
 * @see Expc
 * @see Sinc */
public enum Logc implements ScalarUnaryOperator {
  FUNCTION;

  private static final ScalarUnaryOperator SERIES = //
      Series.of(Tensors.vector(i -> RationalScalar.of(Integers.isEven(i) ? 1 : -1, i + 1), 10));

  @Override
  public Scalar apply(Scalar lambda) {
    Scalar den = lambda.subtract(RealScalar.ONE);
    return Chop._10.isZero(den) //
        ? SERIES.apply(den)
        : Log.FUNCTION.apply(lambda).divide(den);
  }
}
