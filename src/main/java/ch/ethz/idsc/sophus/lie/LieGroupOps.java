// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** utility class to perform simultaneous transformations */
public class LieGroupOps implements Serializable {
  private final LieGroup lieGroup;

  public LieGroupOps(LieGroup lieGroup) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
  }

  public Tensor combine(Tensor g, Tensor h) {
    return lieGroup.element(g).combine(h);
  }

  public Tensor invert(Tensor g) {
    return lieGroup.element(g).inverse().toCoordinate();
  }

  public Tensor allInvert(Tensor sequence) {
    return Tensor.of(sequence.stream().map(this::invert));
  }

  public Tensor allRight(Tensor sequence, Tensor shift) {
    return Tensor.of(sequence.stream() //
        .map(lieGroup::element) //
        .map(lieGroupElement -> lieGroupElement.combine(shift)));
  }

  public Tensor allLeft(Tensor sequence, Tensor shift) {
    LieGroupElement lieGroupElement = lieGroup.element(shift);
    return Tensor.of(sequence.stream() //
        .map(lieGroupElement::combine));
  }

  /** @param g
   * @param h
   * @return g.h.g^-1 */
  public TensorUnaryOperator conjugate(Tensor g) {
    LieGroupElement lieGroupElement = lieGroup.element(g);
    Tensor inverse = lieGroupElement.inverse().toCoordinate();
    return h -> lieGroup.element(lieGroupElement.combine(h)).combine(inverse);
  }

  public Tensor allConjugate(Tensor sequence, Tensor shift) {
    return Tensor.of(sequence.stream().map(conjugate(shift)));
  }
}
