// code by jph
package ch.ethz.idsc.sophus.clt;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import ch.ethz.idsc.sophus.clt.ClothoidSolutions.Search;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Clips;

public class PriorityClothoid implements ClothoidBuilder, Serializable {
  private static final ClothoidSolutions CLOTHOID_SOLUTIONS = ClothoidSolutions.of(Clips.absolute(15.0), 101);

  /** @param comparator
   * @return */
  public static ClothoidBuilder of(Comparator<Clothoid> comparator) {
    return new PriorityClothoid(Objects.requireNonNull(comparator));
  }

  /***************************************************/
  private final Comparator<Clothoid> comparator;

  private PriorityClothoid(Comparator<Clothoid> comparator) {
    this.comparator = comparator;
  }

  @Override
  public Clothoid curve(Tensor p, Tensor q) {
    ClothoidContext clothoidContext = new ClothoidContext(p, q);
    Search search = CLOTHOID_SOLUTIONS.new Search(clothoidContext.s1(), clothoidContext.s2());
    return ClothoidEmit.stream(clothoidContext, search.lambdas()) //
        .min(comparator) //
        .get();
  }

  @Override
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
