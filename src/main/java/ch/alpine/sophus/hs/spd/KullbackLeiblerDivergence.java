// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.exp.Log;

/** References:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 84
 * 
 * "Covariance clustering on Riemannian manifolds for acoustic model compression"
 * by Yusuke Shinohara, Takashi Masuko, and Masami Akamine, 2010 */
public enum KullbackLeiblerDivergence implements TensorMetric {
  INSTANCE;

  @Override
  public Scalar distance(Tensor p1, Tensor p2) {
    SymmetricMatrixQ.require(p1);
    SymmetricMatrixQ.require(p2);
    Tensor s1 = p1.dot(Inverse.of(p2));
    Tensor s2 = p2.dot(Inverse.of(p1));
    return Trace.of(s1.add(s2)).subtract(RealScalar.of(2L * p1.length()));
  }

  /** the implementation is from the paper from Shinohara et al.
   * which probably has some typo?
   * 
   * @param p1
   * @param p2
   * @return squared */
  @PackageTestAccess
  static Scalar between(Tensor p1, Tensor p2) {
    SymmetricMatrixQ.require(p1);
    SymmetricMatrixQ.require(p2);
    Tensor qinvp = LinearSolve.of(p2, p1);
    Scalar n = RealScalar.of(p1.length());
    return Trace.of(qinvp).subtract(n).subtract(Log.FUNCTION.apply(Abs.FUNCTION.apply(Det.of(qinvp)))) //
        .multiply(RationalScalar.HALF);
  }
}
