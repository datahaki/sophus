// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.crv.decim.LineDistance;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Abs;

public enum S2LineDistance implements LineDistance {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new S2Line(p, q);
  }

  private class S2Line implements TensorNorm, Serializable {
    private static final long serialVersionUID = -5686900639748557587L;
    private final Tensor cross;

    public S2Line(Tensor p, Tensor q) {
      cross = NORMALIZE.apply(Cross.of(p, q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      return Abs.between(Pi.HALF, SnMetric.INSTANCE.distance(cross, r));
    }
  }
}