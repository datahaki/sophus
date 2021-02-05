// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Max;

public class SymmetricLineDistance implements LineDistance, Serializable {
  private final LineDistance lineDistance;

  public SymmetricLineDistance(LineDistance lineDistance) {
    this.lineDistance = lineDistance;
  }

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor beg, Tensor end) {
    return new NormImpl( //
        lineDistance.tensorNorm(beg, end), //
        lineDistance.tensorNorm(end, beg));
  }

  private class NormImpl implements TensorNorm, Serializable {
    private final TensorNorm tensorNorm1;
    private final TensorNorm tensorNorm2;

    public NormImpl(TensorNorm tensorNorm1, TensorNorm tensorNorm2) {
      this.tensorNorm1 = tensorNorm1;
      this.tensorNorm2 = tensorNorm2;
    }

    @Override
    public Scalar norm(Tensor index) {
      return Max.of( //
          tensorNorm1.norm(index), //
          tensorNorm2.norm(index));
    }
  }
}
