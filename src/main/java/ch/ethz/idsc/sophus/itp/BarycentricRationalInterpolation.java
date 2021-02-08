// code by jph
package ch.ethz.idsc.sophus.itp;

import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.OrderedQ;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.io.ScalarArray;
import ch.ethz.idsc.tensor.itp.InterpolatingPolynomial;

/** affine weights that satisfy the barycentric equation, i.e. reproduce linear functions
 * 
 * References:
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
 * by J.-P. Berrut, H. D. Mittelmann, 1997
 * 
 * "Pyramid algorithms for barycentric rational interpolation"
 * by Kai Hormann, Scott Schaefer, 2016
 * 
 * @see InterpolatingPolynomial */
public class BarycentricRationalInterpolation implements ScalarTensorFunction {
  /** sign toggle is cancelled in normalization */
  private static final Scalar[] SIGNUM = { RealScalar.ONE, RealScalar.ONE.negate() };

  /** @param knots vector with increasing entries
   * @param degree non-negative
   * @return
   * @throws Exception if knots is not a vector with increasing entries */
  public static ScalarTensorFunction of(Tensor knots, int degree) {
    return new BarycentricRationalInterpolation( //
        OrderedQ.require(knots), //
        Math.min(knots.length() - 1, Integers.requirePositiveOrZero(degree)));
  }

  /***************************************************/
  private final Scalar[] knots;
  private final Scalar[] w;

  private BarycentricRationalInterpolation(Tensor _knots, int degree) {
    knots = ScalarArray.ofVector(_knots);
    w = new Scalar[knots.length];
    for (int k = 0; k < knots.length; ++k) {
      int imin = Math.max(k - degree, 0);
      int imax = knots.length - degree <= k//
          ? knots.length - degree - 1
          : k;
      Scalar temp = SIGNUM[imin & 1];
      Scalar sum = RealScalar.ZERO;
      Scalar knots_k = knots[k];
      for (int i = imin; i <= imax; ++i) {
        final int fk = k;
        Scalar term = IntStream.rangeClosed(i, Math.min(i + degree, knots.length - 1)) //
            .filter(j -> j != fk) //
            .mapToObj(j -> knots[j]) //
            .map(knots_k::subtract) //
            .reduce(Scalar::multiply) //
            .orElse(RealScalar.ONE) //
            .under(temp);
        temp = temp.negate();
        sum = sum.add(term);
      }
      w[k] = sum;
    }
  }

  @Override
  public Tensor apply(Scalar x) {
    Tensor weights = Tensors.reserve(knots.length);
    for (int i = 0; i < knots.length; ++i) {
      Scalar h = x.subtract(knots[i]);
      if (Scalars.isZero(h))
        return UnitVector.of(knots.length, i);
      weights.append(w[i].divide(h));
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
