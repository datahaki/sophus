// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.lie.LieDifferences;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.AdjacentReduce;
import ch.alpine.tensor.alg.Differences;

/** EXPERIMENTAL
 * 
 * <pre>
 * HsDifferences[{a, b, c, d, e}] == {{a, log_a[b]}, {b, log_b[c]}, ..., {d, log_d[e]}}
 * </pre>
 * 
 * Careful:
 * if a != b then log_a[b] is from a different tangent space than log_b[c]
 * and parallel transport can be used to process the tangent vectors.
 * 
 * @see Differences
 * @see LieDifferences */
public final class HsDifferences extends AdjacentReduce {
  private final HomogeneousSpace homogeneousSpace;

  /** @param homogeneousSpace non-null */
  public HsDifferences(HomogeneousSpace homogeneousSpace) {
    this.homogeneousSpace = Objects.requireNonNull(homogeneousSpace);
  }

  @Override
  protected Tensor reduce(Tensor p, Tensor q) {
    return Tensors.of(p, homogeneousSpace.exponential(p).log(q));
  }
}
