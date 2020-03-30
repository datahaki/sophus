// code by jph
package ch.ethz.idsc.sophus.lie.sl2;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.Sinhc;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Cosh;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Reference:
 * "Lie Group methods", p. 313, 334
 * by Arieh Iserles, Hans Z. Munthe-Kaas, Syvert P. Nørsett, Antonella Zanna */
public enum Sl2MatrixExponential implements Exponential {
  INSTANCE;

  private static final Tensor ID = IdentityMatrix.of(2);

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    // x = {{a, b}, {c, -a}}
    Scalar a = x.Get(0, 0);
    Tolerance.CHOP.requireClose(a, x.get(1, 1).negate());
    Scalar b = x.Get(0, 1);
    Scalar c = x.Get(1, 0);
    Scalar omega = Sqrt.FUNCTION.apply(a.multiply(a).add(b.multiply(c)));
    return ID.multiply(Cosh.FUNCTION.apply(omega)).add(x.multiply(Sinhc.FUNCTION.apply(omega)));
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    Tolerance.CHOP.requireClose(Det.of(g), RealScalar.ONE);
    // TODO there should be a specialized formula!
    return MatrixLog.of(g);
  }
}
