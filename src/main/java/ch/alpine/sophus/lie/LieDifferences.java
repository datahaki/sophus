// code by jph
package ch.alpine.sophus.lie;

import java.util.Objects;

import ch.alpine.tensor.alg.AdjacentReduce;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** LieDifferences is the generalization of {@link Differences}
 * The input are elements from the Lie group.
 * The return sequence consists entirely of elements from the Lie algebra,
 * i.e. the tangent space TeG.
 * 
 * <pre>
 * LieDifferences[{a, b, c, d, e}] == {log[a^-1.b], log[b^-1.c], log[c^-1.d], log[d^-1.e]}
 * </pre>
 * 
 * @see Differences */
public enum LieDifferences {
  ;
  public static TensorUnaryOperator of(LieGroup lieGroup) {
    Objects.requireNonNull(lieGroup);
    return new AdjacentReduce((p, q) -> lieGroup.exponential(p).log(q));
  }
}
