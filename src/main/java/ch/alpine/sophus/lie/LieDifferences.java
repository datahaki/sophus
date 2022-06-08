// code by jph
package ch.alpine.sophus.lie;

import java.util.Objects;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.AdjacentReduce;
import ch.alpine.tensor.alg.Differences;

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
public final class LieDifferences extends AdjacentReduce {
  private final LieGroup lieGroup;

  /** @param lieGroup
   * @throws Exception if either parameter is null */
  public LieDifferences(LieGroup lieGroup) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
  }

  /** @param p element of the lie group
   * @param q element of the lie group
   * @return vector == log(p^-1 . q) so that exp(vector) == p^-1 . q */
  @Override
  protected Tensor reduce(Tensor p, Tensor q) {
    return lieGroup.exponential(p).log(q);
  }
}
