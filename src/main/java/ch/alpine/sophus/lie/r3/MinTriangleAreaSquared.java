// code by jph
package ch.alpine.sophus.lie.r3;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.sophus.math.LagrangeMultiplier;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.itp.Fit;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.red.ScalarSummaryStatistics;

/** Quote:
 * "the minimizer of the sum of squared triangle areas of the induced triangle fan."
 * 
 * Reference:
 * "Polygon Laplacian Made Simple"
 * by Astrid Bunge, Philipp Herholz, Misha Kazhdan, Mario Botsch, 2020
 * 
 * Remark: the matrix also appears in Kriging
 * 
 * @see Fit#lagrange(Tensor, Tensor, Tensor, Tensor) */
public enum MinTriangleAreaSquared implements Genesis {
  INSTANCE;

  /** @param polygon with dimensions n x 3
   * @return affine weight vector of length n
   * @throws Exception if given polygon is empty */
  @Override
  public Tensor origin(Tensor polygon) {
    int n = polygon.length();
    Tensor Aeye = Array.zeros(n, n); // symmetric
    Tensor btar = Array.zeros(n);
    Tensor[] x = normalize(polygon).stream().toArray(Tensor[]::new);
    for (int k = 0; k < n; ++k) {
      Tensor xkd = x[(k + 1) % n].subtract(x[k]);
      Tensor vk = Cross.of(x[k], xkd);
      for (int i = 0; i < n; ++i) {
        Tensor vi = Cross.of(x[i], xkd);
        for (int j = 0; j < n; ++j)
          Aeye.set(Cross.of(x[j], xkd).dot(vi)::add, i, j);
        btar.set(vk.dot(vi)::add, i);
      }
    }
    // SymmetricMatrixQ.require(Aeye);
    Tensor Aeqs = ConstantArray.of(RealScalar.ONE, 1, n);
    Tensor brhs = Tensors.of(RealScalar.ONE);
    /* LeastSquares.usingSvd is required, i.e. linear solve does not work */
    Tensor sol = new LagrangeMultiplier(Aeye, btar, Aeqs, brhs).usingSvd();
    /* normalize total for improved numerics */
    return NormalizeTotal.FUNCTION.apply(sol);
  }

  /** @param polygon
   * @return normalized polygon with mean zero and unit average lever length */
  @PackageTestAccess
  static Tensor normalize(Tensor polygon) {
    polygon = Tensor.of(polygon.stream().map(Mean.of(polygon).negate()::add));
    ScalarSummaryStatistics scalarSummaryStatistics = polygon.stream() //
        .map(Vector2Norm::of) //
        .collect(ScalarSummaryStatistics.collector());
    Scalar scalar = scalarSummaryStatistics.getAverage();
    return Scalars.isZero(scalar) //
        ? polygon
        : polygon.divide(scalar);
  }
}
