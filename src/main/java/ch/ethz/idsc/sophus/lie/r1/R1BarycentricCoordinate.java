// code by jph
package ch.ethz.idsc.sophus.lie.r1;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.DeterminateScalarQ;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.OrderedQ;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;

/** References:
 * "Barycentric Rational Interpolation"
 * in NR, 2007
 * 
 * "Barycentric Rational Interpolation with No Poles and High Rates of Approximation"
 * by M.S. Floater, K. Hormann, 2007
 * 
 * "Barycentric Lagrange Interpolation"
 * by J.-P. Berrut, L.N. Trefethen, 2004
 * 
 * "Lebesgue constant minimizing linear rational interpolation of continuous functions over the interval"
 * by J.-P. Berrut, H. D. Mittelmann, 1997 */
public class R1BarycentricCoordinate implements ScalarTensorFunction {
  /** @param domain vector with increasing entries
   * @param degree non-negative
   * @return */
  public static ScalarTensorFunction of(Tensor domain, int degree) {
    return new R1BarycentricCoordinate( //
        OrderedQ.require(VectorQ.require(domain)), //
        Math.min(domain.length() - 1, Integers.requirePositiveOrZero(degree)));
  }

  /***************************************************/
  private final Tensor domain;
  private final int n;
  private final Tensor w;

  private R1BarycentricCoordinate(Tensor _domain, int d) {
    domain = _domain.copy();
    n = domain.length();
    if (n <= d)
      throw new IllegalArgumentException("n=" + n + ", d=" + d);
    w = Tensors.reserve(n);
    for (int k = 0; k < n; ++k) {
      int imin = Math.max(k - d, 0);
      int imax = k >= n - d ? n - d - 1 : k;
      Scalar temp = (imin & 1) == 1 ? RealScalar.ONE.negate() : RealScalar.ONE;
      Scalar sum = RealScalar.ZERO;
      for (int i = imin; i <= imax; ++i) {
        int jmax = Math.min(i + d, n - 1);
        Scalar term = RealScalar.ONE;
        for (int j = i; j <= jmax; ++j) {
          if (j == k)
            continue;
          term = term.multiply(domain.Get(k).subtract(domain.get(j)));
        }
        term = temp.divide(term);
        temp = temp.negate();
        sum = sum.add(term);
      }
      w.append(sum);
    }
  }

  @Override
  public Tensor apply(Scalar x) {
    Tensor weights = Tensors.reserve(n);
    for (int i = 0; i < n; ++i) {
      Scalar h = x.subtract(domain.get(i));
      if (Scalars.isZero(h))
        return UnitVector.of(n, i);
      Tensor weight = w.get(i).divide(h);
      // TODO check in code base where NumberQ is needed vs. DetScQ
      if (!DeterminateScalarQ.of(weight))
        return UnitVector.of(n, i);
      weights.append(weight);
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
