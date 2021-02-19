// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Fold;
import ch.ethz.idsc.tensor.lie.r2.AngleVector;

public enum SnEulerAngle {
  ;
  /** Example: for vector of angles {a0, a1} the resulting coordinate
   * is a point on S^2
   * <pre>
   * Cos a1 Cos a0
   * Cos a1 Sin a0
   * Sin a1
   * </pre>
   * 
   * @param angles vector of length n
   * @return vector of length n + 1 */
  public static Tensor of(Tensor angles) {
    return Fold.of(SnEulerAngle::operator, Tensors.vector(1), angles);
  }

  private static Tensor operator(Tensor tensor, Tensor angle) {
    Tensor cossin = AngleVector.of((Scalar) angle);
    return tensor.multiply(cossin.Get(0)).append(cossin.Get(1));
  }
}
