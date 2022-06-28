// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.Max;

public record SymmetricLineDistance(LineDistance lineDistance) implements LineDistance, Serializable {

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor beg, Tensor end) {
    return new NormImpl( //
        lineDistance.tensorNorm(beg, end), //
        lineDistance.tensorNorm(end, beg));
  }
  private static record NormImpl(TensorNorm tensorNorm1, TensorNorm tensorNorm2) //
      implements TensorNorm {
    @Override
    public Scalar norm(Tensor index) {
      return Max.of( //
          tensorNorm1.norm(index), //
          tensorNorm2.norm(index));
    }
  }
}
