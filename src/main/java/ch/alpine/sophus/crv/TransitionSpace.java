// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.sophus.crv.clt.ClothoidTransitionSpace;
import ch.alpine.sophus.crv.dub.DubinsTransitionSpace;
import ch.alpine.sophus.lie.rn.RnTransitionSpace;
import ch.alpine.tensor.Tensor;

/** TransitionSpace is a factory for {@link Transition}s
 * An instance of TransitionSpace is immutable.
 * 
 * @see RnTransitionSpace
 * @see DubinsTransitionSpace
 * @see ClothoidTransitionSpace */
@FunctionalInterface
public interface TransitionSpace {
  /** @param head state
   * @param tail state
   * @return transition that represents the (unique) connection between the start and end state */
  Transition connect(Tensor head, Tensor tail);
}
