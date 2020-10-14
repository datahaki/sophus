// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Join;

/** closed non-intersecting curve
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public enum HilbertPolygon {
  ;
  /** @param n positive
   * @return */
  public static Tensor of(int n) {
    Integers.requirePositive(n);
    Tensor ante = Tensors.vector((1 << n), 0);
    Tensor post = Tensors.vector(1, 0);
    Tensor tensor = HilbertCurve.of(n);
    if (n % 2 == 0)
      return Join.of(Tensors.of(ante), tensor, Tensors.of(post));
    tensor.set(ante, 0);
    tensor.set(post, tensor.length() - 1);
    return tensor;
  }
}
