// code by jph
package ch.alpine.sophus.clt;

import java.io.Serializable;
import java.util.Comparator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Clip;

public record PriorityClothoid(Comparator<Clothoid> comparator, Clip clip) implements ClothoidBuilder, Serializable {
  @Override
  public Clothoid curve(Tensor p, Tensor q) {
    ClothoidContext clothoidContext = new ClothoidContext(p, q);
    ClothoidSolutions clothoidSolutions = //
        new ClothoidSolutions(ClothoidTangentDefect.of(clothoidContext), clip);
    return ClothoidEmit.stream(clothoidContext, clothoidSolutions.lambdas()) //
        .min(comparator) //
        .orElseThrow();
  }
}
