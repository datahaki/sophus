// code by jph
package ch.ethz.idsc.sophus.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

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
      return VectorNorm2.of(hsManifold.exponential(c).log(r));
    }
  }
}
