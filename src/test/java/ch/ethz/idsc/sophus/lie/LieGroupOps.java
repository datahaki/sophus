// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;

public class LieGroupOps implements Serializable {
  private final LieGroup lieGroup;

  public LieGroupOps(LieGroup lieGroup) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
  }

  public Tensor invertAll(Tensor sequence) {
    return Tensor.of(sequence.stream() //
        .map(lieGroup::element) //
        .map(LieGroupElement::inverse) //
        .map(LieGroupElement::toCoordinate));
  }

  public Tensor right(Tensor sequence, Tensor shift) {
    return Tensor.of(sequence.stream() //
        .map(lieGroup::element) //
        .map(lieGroupElement -> lieGroupElement.combine(shift)));
  }

  public Tensor left(Tensor sequence, Tensor shift) {
    LieGroupElement lieGroupElement = lieGroup.element(shift);
    return Tensor.of(sequence.stream() //
        .map(lieGroupElement::combine));
  }
}
