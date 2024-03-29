// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.tensor.Tensor;

/** utility class to perform simultaneous transformations */
public class LieGroupOps implements Serializable {
  private final LieGroup lieGroup;

  public LieGroupOps(LieGroup lieGroup) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
  }

  /** @param g
   * @return h -> g.h */
  public TensorMapping actionL(Tensor g) {
    return lieGroup.element(g)::combine;
  }

  /** @param g
   * @return h -> h.g */
  public TensorMapping actionR(Tensor g) {
    return tensor -> lieGroup.element(tensor).combine(g);
  }

  /** @param g
   * @return h -> g.h.g^-1 */
  public TensorMapping conjugation(Tensor g) {
    LieGroupElement lieGroupElement = lieGroup.element(g);
    Tensor inverse = lieGroupElement.inverse().toCoordinate();
    return tensor -> lieGroup.element(lieGroupElement.combine(tensor)).combine(inverse);
  }

  /** @return h -> h^-1 */
  public TensorMapping inversion() {
    return tensor -> lieGroup.element(tensor).inverse().toCoordinate();
  }

  // ---
  /** Hint: function is intended to use in tests to assert biinvariance
   * 
   * @param g
   * @return */
  public Collection<TensorMapping> biinvariant(Tensor g) {
    return List.of(actionL(g), actionR(g), conjugation(g), inversion());
  }
}
