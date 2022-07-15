// code by jph
package ch.alpine.sophus.gbc.it;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;

public record WeightsFactors(Tensor weights, Tensor factors) implements Serializable {
  public WeightsFactors {
    VectorQ.requireLength(weights, VectorQ.require(factors).length());
  }
}
