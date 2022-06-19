// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.api.GroupElement;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.num.Cycles;

public class Permutation implements GroupElement, Serializable {
  private final Cycles cycles;

  public Permutation(Cycles cycles) {
    this.cycles = Objects.requireNonNull(cycles);
  }

  @Override // from GroupElement
  public Tensor toCoordinate() {
    return cycles.toTensor();
  }

  @Override // from GroupElement
  public Permutation inverse() {
    return new Permutation(cycles.inverse());
  }

  @Override // from GroupElement
  public Tensor combine(Tensor tensor) {
    return cycles.combine(Cycles.of(tensor)).toTensor();
  }
}
