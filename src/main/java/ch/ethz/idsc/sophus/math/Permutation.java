// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.num.Cycles;

public class Permutation implements GroupElement, Serializable {
  private static final long serialVersionUID = 2955360490615863529L;
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
