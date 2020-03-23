// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.sophus.math.HeadTailInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.InterpolatingPolynomial;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Reference: U. Reif slides */
public class Clothoid implements Serializable {
  private static final Tensor KNOTS = Tensors.vector(0.0, 0.5, 1.0);
  // ---
  private final Tensor pxy;
  private final Tensor diff;
  private final Scalar da;
  private final Scalar b0;
  private final Scalar b1;
  private final Scalar bm;

  /** @param p vector of the form {px, py, pa}
   * @param q vector of the form {qx, qy, qa} */
  public Clothoid(Tensor p, Tensor q) {
    pxy = p.extract(0, 2);
    Scalar pa = p.Get(2);
    Tensor qxy = q.extract(0, 2);
    Scalar qa = q.Get(2);
    // ---
    diff = qxy.subtract(pxy);
    da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    b0 = So2.MOD.apply(pa.subtract(da)); // normal form T0 == b0
    b1 = So2.MOD.apply(qa.subtract(da)); // normal form T1 == b1
    bm = ClothoidApproximation.f(b0, b1);
  }

  public final class Curve implements ScalarTensorFunction {
    /** quadratic polynomial */
    private final ScalarUnaryOperator scalarUnaryOperator = //
        InterpolatingPolynomial.scalar(KNOTS, Tensors.of(b0, bm, b1));
    private final ClothoidIntegral clothoidIntegral = //
        new Legendre3ClothoidIntegral(scalarUnaryOperator);

    @Override
    public Tensor apply(Scalar t) {
      Scalar il = clothoidIntegral.il(t);
      Scalar ir = clothoidIntegral.ir(t);
      /** ratio z enforces interpolation of terminal points
       * t == 0 -> (0, 0)
       * t == 1 -> (1, 0) */
      Scalar z = il.divide(il.add(ir));
      return pxy.add(prod(z, diff)) //
          .append(da.add(scalarUnaryOperator.apply(t)));
    }

    /** @return approximate length */
    public Scalar length() {
      return Norm._2.ofVector(diff).divide(clothoidIntegral.one().abs());
    }
  }

  /** when the start and end point of the clothoid have identical (x, y)-coordinates,
   * the curvature evaluates to Infinity, or NaN. */
  public final class Curvature implements ScalarUnaryOperator, HeadTailInterface {
    private final LagrangeQuadraticD lagrangeQuadraticD = new LagrangeQuadraticD(b0, bm, b1);
    private final Scalar v = Norm._2.ofVector(diff);

    @Override
    public Scalar apply(Scalar t) {
      return lagrangeQuadraticD.apply(t).divide(v);
    }

    @Override // from HeadTailInterface
    public Scalar head() {
      return lagrangeQuadraticD.head().divide(v);
    }

    @Override // from HeadTailInterface
    public Scalar tail() {
      return lagrangeQuadraticD.tail().divide(v);
    }
  }

  /** complex multiplication between z and vector[0]+i*vector[1]
   * 
   * @param z
   * @param vector of length 2 with entries that may be {@link Quantity}
   * @return vector of length 2 with real entries corresponding to real and imag of result */
  /* package */ static Tensor prod(Scalar z, Tensor vector) {
    Scalar zr = Real.FUNCTION.apply(z);
    Scalar zi = Imag.FUNCTION.apply(z);
    Scalar v0 = vector.Get(0);
    Scalar v1 = vector.Get(1);
    return Tensors.of( //
        zr.multiply(v0).subtract(zi.multiply(v1)), //
        zi.multiply(v0).add(zr.multiply(v1)) //
    );
  }
}
