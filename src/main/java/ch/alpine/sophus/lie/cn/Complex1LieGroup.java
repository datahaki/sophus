// code by jph
package ch.alpine.sophus.lie.cn;

import ch.alpine.sophus.math.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

// TODO SOPHUS review: the 1 in the name is not warranted
public enum Complex1LieGroup implements GeodesicSpace {
  INSTANCE;

  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor delta = q.subtract(p);
    return scalar -> p.add(delta.multiply(scalar));
  }
}
