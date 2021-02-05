// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

public class HsProjectedLineDistance implements LineDistance, Serializable {
  private final HsExponential hsExponential;
  private final HsLineProjection hsLineProjection;

  public HsProjectedLineDistance(HsExponential hsExponential) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
    hsLineProjection = new HsLineProjection(hsExponential);
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
      return Norm._2.ofVector(hsExponential.exponential(c).log(r));
    }
  }
}
