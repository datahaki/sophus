// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.itp.LinearBinaryAverage;

public enum BSplineLimitMatrix {
  ;
  /** @param n
   * @param degree
   * @return row-stochastic matrix with dimensions n x n */
  public static Tensor string(int n, int degree) {
    Integers.requirePositive(n);
    Tensor domain = Range.of(0, n);
    return Transpose.of(Tensors.vector(k -> //
    domain.map(GeodesicBSplineFunction.of(LinearBinaryAverage.INSTANCE, degree, UnitVector.of(n, k))), n));
  }
  // public static Tensor string(int n, int degree) {
  // Tensor domain = Range.of(0, n);
  // return Tensor.of(IdentityMatrix.of(n).stream() //
  // .map(row -> domain.map(GeodesicBSplineFunction.of(RnGeodesic.INSTANCE, degree, row))));
  // }
}
