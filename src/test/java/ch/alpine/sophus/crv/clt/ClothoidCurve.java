// code by ureif
package ch.alpine.sophus.crv.clt;

import ch.alpine.sophus.crv.clt.mid.OriginalApproximation;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** Reference:
 * Ulrich Reif slides */
/* package */ abstract class ClothoidCurve implements ScalarTensorFunction {
  protected static final Scalar _1 = RealScalar.of(1.0);
  // ---
  private final Tensor pxy;
  private final Tensor diff;
  private final Scalar da;
  protected final ScalarUnaryOperator lagrangeQuadratic;

  public ClothoidCurve(Tensor p, Tensor q) {
    pxy = p.extract(0, 2);
    Scalar pa = p.Get(2);
    Tensor qxy = q.extract(0, 2);
    Scalar qa = q.Get(2);
    // ---
    diff = qxy.subtract(pxy);
    da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    Scalar b0 = So2.MOD.apply(pa.subtract(da)); // normal form T0 == b0
    Scalar b1 = So2.MOD.apply(qa.subtract(da)); // normal form T1 == b1
    // ---
    lagrangeQuadratic = LagrangeQuadratic.interp(b0, OriginalApproximation.INSTANCE.apply(b0, b1), b1);
  }

  /** @param t
   * @return approximate integration of exp i*clothoidQuadratic on [0, t] */
  protected abstract Scalar il(Scalar t);

  /** @param t
   * @return approximate integration of exp i*clothoidQuadratic on [t, 1] */
  protected abstract Scalar ir(Scalar t);

  @Override
  public final Tensor apply(Scalar t) {
    Scalar il = il(t);
    Scalar ir = ir(t);
    /* ratio z enforces interpolation of terminal points
     * t == 0 -> (0, 0)
     * t == 1 -> (1, 0) */
    Scalar z = il.divide(il.add(ir));
    return pxy.add(ClothoidImpl.prod(z, diff)) //
        .append(lagrangeQuadratic.apply(t).add(da));
  }

  public Scalar exp_i(Scalar s) {
    return ComplexScalar.unit(lagrangeQuadratic.apply(s));
  }
}
