// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.TensorBinaryOperator;

public record BchBinaryAverage(TensorBinaryOperator bch) implements GeodesicSpace, Serializable {
  @Override // from GeodesicSpace
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log_pq = bch.apply(p.negate(), q);
    return scalar -> bch.apply(p, log_pq.multiply(scalar));
  }
}
