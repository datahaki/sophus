// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** in geomstats, the corresponding function is "regularize"
 * 
 * @see HnGeodesic */
public enum HnProjection implements TensorUnaryOperator {
  INSTANCE;

  @Override
  public Tensor apply(Tensor x) {
    // TODO not clear from where to where x is projected!?
    Scalar xn2 = LBilinearForm.normSquared(x); // if x in H^n then equals to -1
    xn2 = xn2.negate();
    if (Sign.isNegative(xn2)) {
      // System.err.println(x);
      // System.err.println("negative sign " + xn2);
    }
    // hyperboloid.py uses Abs
    // FIXME
    return x.divide(Sqrt.FUNCTION.apply(Abs.FUNCTION.apply(xn2)));
  }
}
