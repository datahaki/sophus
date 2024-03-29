// code by jph, gjoel
package ch.alpine.sophus.crv.dub;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import ch.alpine.sophus.crv.TransitionSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Sign;

public class DubinsTransitionSpace implements TransitionSpace, Serializable {
  /** @param radius positive
   * @param comparator
   * @return
   * @see DubinsPathComparators */
  public static TransitionSpace of(Scalar radius, Comparator<DubinsPath> comparator) {
    return new DubinsTransitionSpace( //
        Sign.requirePositive(radius), //
        Objects.requireNonNull(comparator));
  }

  // ---
  private final Scalar radius;
  private final Comparator<DubinsPath> comparator;

  private DubinsTransitionSpace(Scalar radius, Comparator<DubinsPath> comparator) {
    this.radius = radius;
    this.comparator = comparator;
  }

  @Override // from TransitionSpace
  public DubinsTransition connect(Tensor head, Tensor tail) {
    return new DubinsTransition(head, tail, dubinsPath(head, tail));
  }

  private DubinsPath dubinsPath(Tensor start, Tensor end) {
    return FixedRadiusDubins.of(start, end, radius).stream().min(comparator).orElseThrow();
  }
}
