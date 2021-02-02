// code by jph
package ch.ethz.idsc.sophus.crv;

import java.util.Iterator;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.itp.InterpolatingPolynomial;

/** @see CurvatureComb */
public enum Curvature2D {
  ;
  private static final InterpolatingPolynomial INTERPOLATING_POLYNOMIAL = //
      InterpolatingPolynomial.of(Tensors.vector(1, 2, 3));
  private static final Scalar LAST = RealScalar.of(4);

  /** @param points of the form {{p1x, p1y}, {p2x, p2y}, ..., {pNx, pNy}}
   * @return vector */
  public static Tensor string(Tensor points) {
    int length = points.length();
    Tensor vector = Array.zeros(length);
    if (2 < length) {
      Iterator<Tensor> iterator = points.iterator();
      Tensor p = iterator.next();
      Tensor q = iterator.next();
      int index = 0;
      while (iterator.hasNext())
        vector.set( //
            SignedCurvature2D.of(p, p = q, q = iterator.next()).orElse(RealScalar.ZERO), //
            ++index);
      int last = length - 1;
      if (4 < length) {
        vector.set(INTERPOLATING_POLYNOMIAL.scalarUnaryOperator(vector.extract(1, 4)).apply(RealScalar.ZERO), 0);
        vector.set(INTERPOLATING_POLYNOMIAL.scalarUnaryOperator(vector.extract(length - 4, last)).apply(LAST), last);
      } else {
        vector.set(vector.Get(1), 0);
        vector.set(vector.Get(length - 2), last);
      }
    }
    return vector;
  }
}
