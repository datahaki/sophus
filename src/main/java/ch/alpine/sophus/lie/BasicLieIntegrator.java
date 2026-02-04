package ch.alpine.sophus.lie;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;

public record BasicLieIntegrator(LieGroup lieGroup) implements LieIntegrator, Serializable {
  @Override
  public Tensor spin(Tensor g, Tensor x) {
    return lieGroup.exponential(g).exp(x);
  }
}
