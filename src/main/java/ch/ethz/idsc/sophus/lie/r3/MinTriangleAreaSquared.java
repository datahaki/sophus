// code by jph
package ch.ethz.idsc.sophus.lie.r3;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.mat.LeastSquares;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.ScalarSummaryStatistics;

/** Quote:
 * "the minimizer of the sum of squared triangle areas of the induced triangle fan."
 * 
 * Reference:
 * "Polygon Laplacian Made Simple"
 * by Astrid Bunge, Philipp Herholz, Misha Kazhdan, Mario Botsch, 2020
 * 
 * Remark: the matrix also appears in Kriging */
public enum MinTriangleAreaSquared {
  ;
  private static final Scalar HALF = RealScalar.of(0.5);

  /** @param polygon with dimensions n x 3
   * @return affine weight vector of length n
   * @throws Exception if given polygon is empty */
  public static Tensor weights(Tensor polygon) {
    int n = Integers.requirePositive(polygon.length());
    Tensor A = Array.zeros(n + 1, n + 1); // symmetric
    Tensor b = Array.zeros(n + 1);
    Tensor[] x = normalize(polygon).stream().toArray(Tensor[]::new);
    for (int k = 0; k < n; ++k) {
      Tensor xkd = x[(k + 1) % n].subtract(x[k]);
      Tensor vk = Cross.of(x[k], xkd);
      for (int i = 0; i < n; ++i) {
        Tensor vi = Cross.of(x[i], xkd);
        for (int j = 0; j < n; ++j)
          A.set(Cross.of(x[j], xkd).dot(vi)::add, i, j);
        b.set(vk.dot(vi)::add, i);
      }
      A.set(HALF, k, n);
      A.set(HALF, n, k);
    }
    b.set(HALF, n);
    return LeastSquares.usingSvd(A, b).extract(0, n);
  }

  /** @param polygon
   * @return */
  /* package */ static Tensor normalize(Tensor polygon) {
    polygon = Tensor.of(polygon.stream().map(Mean.of(polygon)::subtract));
    ScalarSummaryStatistics scalarSummaryStatistics = //
        polygon.stream().map(Norm._2::ofVector).collect(ScalarSummaryStatistics.collector());
    Scalar scalar = scalarSummaryStatistics.getAverage();
    return Scalars.isZero(scalar) //
        ? polygon
        : polygon.divide(scalar);
  }
}
