// code by jph
package ch.alpine.sophus.hs.s2;

import java.io.Serializable;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Abs;

public enum S2LineDistance implements LineDistance {
  INSTANCE;

  @Override
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new S2Line(p, q);
  }

  private class S2Line implements TensorNorm, Serializable {
    private final Tensor cross;

    public S2Line(Tensor p, Tensor q) {
      cross = Vector2Norm.NORMALIZE.apply(Cross.of(p, q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      return Abs.between(Pi.HALF, SnManifold.INSTANCE.distance(cross, r));
    }
  }
}