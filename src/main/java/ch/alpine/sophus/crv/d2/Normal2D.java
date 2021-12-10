// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;

public enum Normal2D {
  ;
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // TODO probably not a good general choice if units are involved
  private static final Tensor ZEROS = Array.zeros(2);

  /** @param points of the form {{p1x, p1y}, {p2x, p2y}, ..., {pNx, pNy}}
   * @return matrix of the form {{n1x, n1y}, {n2x, n2y}, ..., {nNx, nNy}} unitless */
  public static Tensor string(Tensor points) {
    Tensor normal = Tensors.empty();
    int length = points.length();
    if (2 < length) {
      Tensor a = points.get(0);
      Tensor b = points.get(1);
      normal.append(process(b.subtract(a)));
    } else //
    if (0 < length)
      normal.append(ZEROS);
    for (int index = 1; index < length - 1; ++index) {
      Tensor a = points.get(index - 1);
      Tensor c = points.get(index + 1);
      normal.append(process(c.subtract(a)));
    }
    if (2 < length) {
      Tensor b = points.get(length - 2);
      Tensor c = points.get(length - 1);
      normal.append(process(c.subtract(b)));
    } else //
    if (1 < length)
      normal.append(ZEROS);
    return normal;
  }

  private static Tensor process(Tensor tangent) {
    return NORMALIZE_UNLESS_ZERO.apply(Cross.of(tangent));
  }
}
