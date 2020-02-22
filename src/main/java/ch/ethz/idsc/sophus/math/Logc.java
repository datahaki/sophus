// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sinc;

/** Log [ Lambda ] / ( Lambda - 1 )
 *
 * @see Expc
 * @see Sinc */
public enum Logc implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar lambda) {
    Scalar den = lambda.subtract(RealScalar.ONE);
    return Scalars.isZero(den) //
        ? RealScalar.ONE
        : Log.FUNCTION.apply(lambda).divide(den);
  }
}
