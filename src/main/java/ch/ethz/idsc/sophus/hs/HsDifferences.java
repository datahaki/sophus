// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Differences;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** HsDifferences is the generalization of {@link Differences}
 * The input are elements from the Lie group.
 * The return sequence consists of elements from the Lie algebra.
 * 
 * <pre>
 * HsDifferences[{a, b, c, d, e}] == {log a^-1.b, log b^-1.c, log c^-1.d, log d^-1.e}
 * </pre> */
public final class HsDifferences implements TensorUnaryOperator {
  private static final long serialVersionUID = -2623684602810832239L;
  // ---
  private final HsExponential hsExponential;

  /** @param hsExponential
   * @throws Exception if either parameter is null */
  public HsDifferences(HsExponential hsExponential) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Tensor result = Tensors.reserve(tensor.length() - 1);
    Tensor prev = tensor.get(0);
    for (int index = 1; index < tensor.length(); ++index)
      result.append(pair(prev, prev = tensor.get(index)));
    return result;
  }

  /** @param p element of the lie group
   * @param q element of the lie group
   * @return vector == log(p^-1 . q) so that exp(vector) == p^-1 . q */
  public Tensor pair(Tensor p, Tensor q) {
    return hsExponential.exponential(p).log(q);
  }
}
