// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** utility class to perform simultaneous transformations */
public class LieGroupOps implements Serializable {
  private final LieGroup lieGroup;

  public LieGroupOps(LieGroup lieGroup) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
  }

  /***************************************************/
  public LieGroupOp actionL(Tensor g) {
    return new LieGroupOp(_actionL(g));
  }

  public LieGroupOp actionR(Tensor g) {
    return new LieGroupOp(_actionR(g));
  }

  public LieGroupOp conjugation(Tensor g) {
    return new LieGroupOp(_conjugation(g));
  }

  public LieGroupOp inversion() {
    return new LieGroupOp(_inversion());
  }

  /** Hint: function is intended to use in tests to assert biinvariance
   * 
   * @param g
   * @return */
  public Collection<LieGroupOp> biinvariant(Tensor g) {
    return Arrays.asList(actionL(g), actionR(g), conjugation(g), inversion());
  }

  /***************************************************/
  /** @param g
   * @return h -> g.h */
  private TensorUnaryOperator _actionL(Tensor g) {
    return lieGroup.element(g)::combine;
  }

  /** @param g
   * @return h -> h.g */
  private TensorUnaryOperator _actionR(Tensor g) {
    return tensor -> lieGroup.element(tensor).combine(g);
  }

  /** @param g
   * @return h -> g.h.g^-1 */
  private TensorUnaryOperator _conjugation(Tensor g) {
    LieGroupElement lieGroupElement = lieGroup.element(g);
    Tensor inverse = lieGroupElement.inverse().toCoordinate();
    return tensor -> lieGroup.element(lieGroupElement.combine(tensor)).combine(inverse);
  }

  /** @return h -> h^-1 */
  private TensorUnaryOperator _inversion() {
    return tensor -> lieGroup.element(tensor).inverse().toCoordinate();
  }
}
