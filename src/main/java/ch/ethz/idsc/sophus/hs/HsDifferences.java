// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieDifferences;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Differences;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** EXPERIMENTAL
 * 
 * <pre>
 * HsDifferences[{a, b, c, d, e}] == {{a, log_a[b]}, {b, log_b[c]}, ..., {d, log_d[e]}}
 * </pre>
 * 
 * @see Differences
 * @see LieDifferences */
public final class HsDifferences implements TensorUnaryOperator {
  private final HsManifold hsManifold;

  /** @param hsManifold
   * @throws Exception if either parameter is null */
  public HsDifferences(HsManifold hsManifold) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    List<Tensor> list = new ArrayList<>(tensor.length() - 1);
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor prev = iterator.next();
    while (iterator.hasNext())
      list.add(pair(prev, prev = iterator.next()));
    return Unprotect.using(list);
  }

  /* package */ Tensor pair(Tensor p, Tensor q) {
    return Tensors.of(p, hsManifold.exponential(p).log(q));
  }
}
