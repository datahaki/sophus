// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.Quaternion;

/** Examples of type T are {@link Tensor} and {@link Quaternion} */
@FunctionalInterface
public interface Metric<T> {
  /** a metric satisfies the following conditions
   * 
   * 1. non-negativity or separation axiom
   * 2. identity of indiscernibles
   * 3. symmetry
   * 4. subadditivity or triangle inequality
   * 
   * <p>implementations threat parameters p and q as immutable
   * 
   * @param p
   * @param q
   * @return distance between p and q */
  Scalar distance(T p, T q);
}
