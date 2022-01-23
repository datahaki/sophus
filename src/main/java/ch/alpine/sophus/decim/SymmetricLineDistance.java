// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.math.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.Max;

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

  private record NormImpl(TensorNorm tensorNorm1, TensorNorm tensorNorm2) //
      implements TensorNorm, Serializable {
    @Override
    public Scalar norm(Tensor index) {
      return Max.of( //
          tensorNorm1.norm(index), //
          tensorNorm2.norm(index));
    }
  }
}
