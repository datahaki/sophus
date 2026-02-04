// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.lie.LieDifferences;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.AdjacentReduce;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** <pre>
 * HsDifferences[{a, b, c, d, e}] == {{a, log_a[b]}, {b, log_b[c]}, ..., {d, log_d[e]}}
 * </pre>
 * 
 * Careful:
 * if a != b then log_a[b] is from a different tangent space than log_b[c]
 * and parallel transport can be used to process the tangent vectors.
 * 
 * @see Differences
 * @see LieDifferences */
public enum HsDifferences {
  ;
  public static TensorUnaryOperator of(HomogeneousSpace homogeneousSpace) {
    Objects.requireNonNull(homogeneousSpace);
    return new AdjacentReduce((p, q) -> Tensors.of(p, homogeneousSpace.exponential(p).log(q)));
  }
}
