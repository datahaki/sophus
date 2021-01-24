// code by jph
package ch.ethz.idsc.sophus.hs.h2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.hn.HnAngle;
import ch.ethz.idsc.sophus.lie.r2.Extract2D;
import ch.ethz.idsc.sophus.math.sca.SinhcInverse;
import ch.ethz.idsc.tensor.DeterminateScalarQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.red.Hypot;

public class H2TangentSpace implements TangentSpace, Serializable {
  private static final long serialVersionUID = 5971892826206288161L;
  // ---
  private final Tensor x;

  /** @param x vector of length 3 */
  public H2TangentSpace(Tensor x) {
    this.x = VectorQ.requireLength(x, 3);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    HnAngle hnAngle = new HnAngle(x, y);
    Scalar angle = hnAngle.angle();
    // TODO not elegant
    Tensor ext = y.subtract(x.multiply(hnAngle.cosh_d())).multiply(SinhcInverse.FUNCTION.apply(angle));
    Tensor log = Extract2D.FUNCTION.apply(ext);
    Scalar factor = angle.divide(Hypot.ofVector(log));
    return DeterminateScalarQ.of(factor) //
        ? log.multiply(factor)
        : log;
  }
}
