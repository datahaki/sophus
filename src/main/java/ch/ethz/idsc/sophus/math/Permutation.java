// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.num.Cycles;
import ch.ethz.idsc.tensor.num.InversePermutation;
import ch.ethz.idsc.tensor.num.PermutationProduct;

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
    return new Permutation(InversePermutation.of(cycles));
  }

  @Override // from GroupElement
  public Tensor combine(Tensor tensor) {
    return PermutationProduct.of(cycles, Cycles.of(tensor)).toTensor();
  }
}
