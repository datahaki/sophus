// code by jph
package ch.ethz.idsc.sophus.lie.sl2;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.sca.Sinhc;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.lie.HodgeDual;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Cosh;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** References:
 * "Lie Group methods", p. 313, 334
 * by Arieh Iserles, Hans Z. Munthe-Kaas, Syvert P. Nørsett, Antonella Zanna
 * 
 * Geometry VI Riemannian Geometry
 * Chapter 12. Metric Properties of Geodesics, p.158
 * by M. M. Postnikov */
public enum Sl2MatrixExponential implements Exponential, TangentSpace {
  INSTANCE;

  private static final Tensor ID = IdentityMatrix.of(2);
  private static final Tensor ID_NEGATE = ID.negate();
  private static final Tensor LOG_ID_NEGATE = HodgeDual.of(Pi.VALUE, 2);
  private static final Scalar TWO_NEGATE = RealScalar.of(-2);

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    // x = {{a, b}, {c, -a}}
    Scalar a = x.Get(0, 0);
    Tolerance.CHOP.requireClose(a, x.get(1, 1).negate());
    Scalar b = x.Get(0, 1);
    Scalar c = x.Get(1, 0);
    Scalar omega = Sqrt.FUNCTION.apply(a.multiply(a).add(b.multiply(c)));
    Tensor g = ID.multiply(Cosh.FUNCTION.apply(omega)).add(x.multiply(Sinhc.FUNCTION.apply(omega)));
    Tolerance.CHOP.requireAllZero(g.map(Imag.FUNCTION));
    return g;
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    Tolerance.CHOP.requireClose(Det.of(g), RealScalar.ONE);
    if (Tolerance.CHOP.close(g, ID_NEGATE))
      return LOG_ID_NEGATE;
    Tensor log = MatrixLog.of(g);
    Scalar a = g.Get(0, 0);
    Scalar d = g.Get(1, 1);
    if (Scalars.lessEquals(a.add(d), TWO_NEGATE))
      throw TensorRuntimeException.of(g);
    Chop._04.requireClose(g, exp(log));
    return log;
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    // specific to Sl2, skip 1, or limit 3
    return Tensor.of(log(q).flatten(1).skip(1));
  }
}
