// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.ext.Integers;

/** closed non-intersecting curve
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public enum HilbertPolygon {
  ;
  /** @param n positive
   * @return hilbert polygon with integer coordinates */
  public static Tensor of(int n) {
    Integers.requirePositive(n);
    Tensor ante = Tensors.vector((1 << n), 0);
    Tensor post = Tensors.vector(1, 0);
    Tensor tensor = HilbertCurve.of(n);
    if (Integers.isEven(n))
      return Join.of(Tensors.of(ante), tensor, Tensors.of(post));
    tensor.set(ante, 0);
    tensor.set(post, tensor.length() - 1);
    return tensor;
  }
}
