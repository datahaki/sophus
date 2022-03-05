// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;

public class HsProjectedLineDistance implements LineDistance, Serializable {
  private final HsManifold hsManifold;
  private final HsLineProjection hsLineProjection;

  public HsProjectedLineDistance(HsManifold hsManifold) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
    hsLineProjection = new HsLineProjection(hsManifold);
  }

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor beg, Tensor end) {
    return new NormImpl(beg, end);
  }

  private class NormImpl implements TensorNorm, Serializable {
    private final Tensor beg;
    private final Tensor end;

    public NormImpl(Tensor beg, Tensor end) {
      this.beg = beg;
      this.end = end;
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      Tensor c = hsLineProjection.onto(beg, end, r);
      return Vector2Norm.of(hsManifold.exponential(c).log(r));
    }
  }
}
