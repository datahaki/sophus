// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public enum S2Transport implements HsTransport {
  INSTANCE;

  @Override
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    // return RotationMatrix3D.of(orig, dest)::dot;
    Tensor matrix = RotationMatrix3D.of(orig, dest);
    return vector -> {
      Tolerance.CHOP.requireAllZero(orig.dot(vector));
      return matrix.dot(vector);
    };
  }
}
