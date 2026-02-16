// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.red.Trace;

/** Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 92 */
public class SpdRiemann {
  private final Tensor pi;

  public SpdRiemann(Tensor p) {
    pi = Inverse.of(p);
  }

  public Tensor at(Tensor v, Tensor w, Tensor u) {
    Tensor t1 = Dot.of(w, pi, v, pi, u);
    Tensor t2 = Dot.of(u, pi, v, pi, w);
    Tensor t3 = Dot.of(v, pi, w, pi, u);
    Tensor t4 = Dot.of(u, pi, w, pi, v);
    return t1.add(t2).subtract(t3).subtract(t4).multiply(Rational.of(1, 4));
  }

  /** Reference: 3.3.2.2 Riemannian metric
   * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
   * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 83
   * 
   * @param w1
   * @param w2
   * @return */
  public Scalar scalarProd(Tensor w1, Tensor w2) {
    return Trace.of(pi.dot(w1).dot(pi).dot(w2));
  }

  /** Sectional curvature
   * 
   * @param u
   * @param v
   * @return */
  public Scalar sectional(Tensor u, Tensor v) {
    Scalar num = scalarProd(at(u, v, v), u);
    Scalar uv = scalarProd(u, v);
    Scalar den = scalarProd(u, u).multiply(scalarProd(v, v)).subtract(uv.multiply(uv));
    return num.divide(den);
  }
}
