// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.nrm.VectorNorm2Squared;

/** Poincare ball model
 * 
 * Reference:
 * "Metric Spaces of Non-Positive Curvature" p. 86
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum BnCoordinate {
  ;
  /** @param x in Bn
   * @return coordinate in Hn */
  public static Tensor bnToHn(Tensor x) {
    Scalar xn2 = VectorNorm2Squared.of(x);
    return x.add(x).append(RealScalar.ONE.add(xn2)).divide(RealScalar.ONE.subtract(xn2));
  }

  /** @param x in Hn
   * @return coordinate in Bn */
  public static Tensor hnToBn(Tensor x) {
    return x.extract(0, x.length() - 1).divide(RealScalar.ONE.add(Last.of(x)));
  }
}
