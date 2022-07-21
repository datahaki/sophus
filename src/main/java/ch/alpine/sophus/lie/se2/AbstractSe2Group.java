// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;
import ch.alpine.tensor.sca.tri.Tan;

/** Hint:
 * The angular coordinate is not automatically mapped to [-pi, pi).
 *
 * References:
 * <a href="http://vixra.org/abs/1807.0463">1807.0463</a>
 * <a href="https://www.youtube.com/watch?v=2vDciaUgL4E">video</a> */
public abstract class AbstractSe2Group implements LieGroup {
  private static final Scalar HALF = RealScalar.of(0.5);

  /** maps a vector x from the Lie-algebra se2 to a vector of the Lie-group SE2
   * 
   * @param x element in the se2 Lie-algebra of the form {vx, vy, beta}
   * @return element g in SE2 as vector with coordinates of g == exp x */
  @Override // from Exponential
  public final Tensor exp(Tensor x) {
    Scalar be = x.Get(2);
    if (Scalars.isZero(be))
      return x.copy();
    Scalar vx = x.Get(0);
    Scalar vy = x.Get(1);
    Scalar cd = Cos.FUNCTION.apply(be).subtract(RealScalar.ONE);
    Scalar sd = Sin.FUNCTION.apply(be);
    return Tensors.of( //
        sd.multiply(vx).add(cd.multiply(vy)).divide(be), //
        sd.multiply(vy).subtract(cd.multiply(vx)).divide(be), //
        be);
  }

  /** @param g element in the SE2 Lie group of the form {px, py, beta}
   * @return element x in the se2 Lie algebra with x == log g, and g == exp x */
  @Override // from Exponential
  public final Tensor log(Tensor g) {
    Scalar be = g.Get(2);
    Scalar be2 = be.multiply(HALF);
    Scalar tan = Tan.FUNCTION.apply(be2);
    if (Scalars.isZero(tan))
      return g.copy();
    Scalar x = g.Get(0);
    Scalar y = g.Get(1);
    return Tensors.of( //
        y.add(x.divide(tan)).multiply(be2), //
        y.divide(tan).subtract(x).multiply(be2), //
        be);
  }

  @Override // from Exponential
  public final Tensor vectorLog(Tensor g) {
    return log(g);
  }
}
