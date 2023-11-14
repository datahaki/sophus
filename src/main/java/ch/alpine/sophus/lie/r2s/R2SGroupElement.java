// code by jph
package ch.alpine.sophus.lie.r2s;

import java.io.Serializable;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/** Quote: "RxRxS is a Lie group, and it can be shown using some
 * representation theory that G is not a matrix group"
 * 
 * Reference:
 * "Differential Geometry and Lie Groups A Computational Perspective", p.539
 * by Jean Gallier, Jocelyn Quaintance, 2020 */
public class R2SGroupElement implements LieGroupElement, Serializable {
  private final Scalar x;
  private final Scalar y;
  private final Scalar u;

  public R2SGroupElement(Tensor xyu) {
    x = xyu.Get(0);
    y = xyu.Get(1);
    u = So2.MOD.apply(xyu.Get(2));
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return Tensors.of(x, y, u);
  }

  @Override // from GroupElement
  public Tensor combine(Tensor xyu) {
    Scalar x2 = xyu.Get(0);
    Scalar y2 = xyu.Get(1);
    Scalar u2 = xyu.Get(2);
    return Tensors.of( //
        x.add(x2), //
        y.add(y2), //
        So2.MOD.apply(u.add(u2).add(x.multiply(y2))));
  }

  @Override // from LieGroupElement
  public LieGroupElement inverse() {
    return new R2SGroupElement(Tensors.of( //
        x.negate(), //
        y.negate(), //
        So2.MOD.apply(x.multiply(y).subtract(u))));
  }

  @Override
  public Tensor adjoint(Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}
