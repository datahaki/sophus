// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.crv.decim.LineDistance;
import ch.ethz.idsc.sophus.hs.sn.SnMetric;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.Abs;

public enum S2LineDistance implements LineDistance {
  INSTANCE;

  @Override
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new S2Line(p, q);
  }

  private class S2Line implements TensorNorm, Serializable {
    private final Tensor cross;

    public S2Line(Tensor p, Tensor q) {
      cross = VectorNorm2.NORMALIZE.apply(Cross.of(p, q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      return Abs.between(Pi.HALF, SnMetric.INSTANCE.distance(cross, r));
    }
  }
}