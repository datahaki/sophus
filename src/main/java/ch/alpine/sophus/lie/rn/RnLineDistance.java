// code by jph
package ch.alpine.sophus.lie.rn;

import java.io.Serializable;

import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Projection;

/** consistent with
 * <pre>
 * new HsLineDistance(RnGroup.INSTANCE);
 * </pre> */
public enum RnLineDistance implements LineDistance {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = NormalizeUnlessZero.with(Vector2Norm::of);

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new TensorNormImpl(p, q);
  }

  private static class TensorNormImpl implements TensorNorm, Serializable {
    private final Tensor p;
    private final TensorUnaryOperator projection;

    public TensorNormImpl(Tensor p, Tensor q) {
      this.p = p;
      projection = Projection.on(NORMALIZE.apply(q.subtract(p)));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor d) {
      d = d.subtract(p);
      return Vector2Norm.of(d.subtract(projection.apply(d)));
    }
  }
}
