// code by jph
package ch.alpine.sophus.gbc.it;

import ch.alpine.tensor.Tensor;

public class Evaluation {
  private final Tensor weights;
  private final Tensor factors;

  public Evaluation(Tensor weights, Tensor factors) {
    this.weights = weights;
    this.factors = factors;
  }

  public Tensor factors() {
    return factors;
  }

  public Tensor weights() {
    return weights;
  }
}