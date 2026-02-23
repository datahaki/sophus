// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.math.VectorProjection;
import ch.alpine.sophus.math.api.LineDistance;
import ch.alpine.sophus.math.api.TensorDistance;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;

/** consistent with
 * <pre>
 * new HsLineDistance(RnGroup.INSTANCE);
 * </pre> */
public enum RnLineDistance implements LineDistance {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = NormalizeUnlessZero.with(Vector2Norm::of);

  @Override // from LineDistance
  public TensorDistance distanceToLine(Tensor p, Tensor q) {
    return new TensorNormImpl(p, q);
  }

  private static class TensorNormImpl implements TensorDistance {
    private final Tensor p;
    private final TensorUnaryOperator projection;

    public TensorNormImpl(Tensor p, Tensor q) {
      this.p = p;
      projection = VectorProjection.on(NORMALIZE.apply(q.subtract(p)));
    }

    @Override // from TensorNorm
    public Scalar distance(Tensor d) {
      d = d.subtract(p);
      return Vector2Norm.of(d.subtract(projection.apply(d)));
    }
  }
}
