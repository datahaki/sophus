// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.BinaryAverage;

/** implementations are not required to be symmetric.
 * 
 * Example: For clothoids
 * <pre>
 * midpoint(p, q) != midpoint(q, p)
 * </pre>
 * 
 * When combined with {@link BinaryAverage} the relation should hold
 * <pre>
 * MidpointInterface.midpoint(p, q) == BinaryAverage.split(p, q, 1/2)
 * </pre> */
@FunctionalInterface
public interface MidpointInterface {
  /** @param p
   * @param q
   * @return midpoint along curve from p to q */
  Tensor midpoint(Tensor p, Tensor q);
}
