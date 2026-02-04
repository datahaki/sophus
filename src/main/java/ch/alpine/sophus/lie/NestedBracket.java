// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.tensor.Tensor;

public record NestedBracket(Tensor ad) {
  public Tensor l2r(Tensor x0, Tensor... xs) {
    // TODO SOPHUS require structure of xs non empty
    Tensor y = x0;
    for (Tensor x : xs)
      y = ad.dot(y).dot(x);
    return y;
  }

  public Tensor r2l(Tensor... xs) {
    // TODO SOPHUS require structure of xs non empty
    Tensor y = xs[xs.length - 1];
    for (int index = xs.length - 2; 0 <= index; --index)
      y = ad.dot(xs[index]).dot(y);
    return y;
  }
}
