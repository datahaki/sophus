// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** parallel transport of tangent vector at orig to dest point on manifold S^n */
public enum SnTransport implements HsTransport {
  INSTANCE;

  @Override
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    Tensor matrix = SnRotationMatrix.of(orig, dest);
    return vector -> {
      Tolerance.CHOP.requireAllZero(orig.dot(vector));
      return matrix.dot(vector);
    };
  }
}
