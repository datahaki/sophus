// code by jph
package ch.alpine.sophus.lie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
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
public final class LieDifferences implements TensorUnaryOperator {
  private final LieExponential lieExponential;

  /** @param lieExponential
   * @throws Exception if either parameter is null */
  public LieDifferences(LieExponential lieExponential) {
    this.lieExponential = Objects.requireNonNull(lieExponential);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    List<Tensor> list = new ArrayList<>(tensor.length() - 1);
    Iterator<Tensor> iterator = tensor.iterator();
    for (Tensor prev = iterator.next(); iterator.hasNext();)
      list.add(pair(prev, prev = iterator.next()));
    return Unprotect.using(list);
  }

  /** @param p element of the lie group
   * @param q element of the lie group
   * @return vector == log(p^-1 . q) so that exp(vector) == p^-1 . q */
  /* package */ Tensor pair(Tensor p, Tensor q) {
    return lieExponential.exponential(p).log(q);
  }
}
