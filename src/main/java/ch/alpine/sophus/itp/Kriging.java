// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;

import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.math.LagrangeMultiplier;
import ch.alpine.sophus.math.var.ExponentialVariogram;
import ch.alpine.sophus.math.var.PowerVariogram;
import ch.alpine.sophus.math.var.SphericalVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.qty.Quantity;

/** implementation of kriging for homogeneous spaces
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * <p>Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction, also known in different communities as Gauss-Markov estimation
 * or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 
 * @see PowerVariogram
 * @see ExponentialVariogram
 * @see SphericalVariogram */
public class Kriging implements Serializable {
  /** Gaussian process regression
   * 
   * @param sedarim
   * @param sequence
   * @param values vector or matrix
   * @param covariance symmetric matrix
   * @return */
  public static Kriging regression(Sedarim sedarim, Tensor sequence, Tensor values, Tensor covariance) {
    return of(sedarim, sequence, values, covariance);
  }

  /** @param sedarim
   * @param sequence of points
   * @param values vector or matrix associated to points in given sequence
   * @return */
  public static Kriging interpolation(Sedarim sedarim, Tensor sequence, Tensor values) {
    int n = values.length();
    // TODO SOPHUS UNIT Array.zeros(n, n) may not be sufficiently generic
    return regression(sedarim, sequence, values, Array.zeros(n, n));
  }

  /** uses unit vectors as target values
   * 
   * @param sedarim
   * @param sequence of points
   * @return */
  public static Kriging barycentric(Sedarim sedarim, Tensor sequence) {
    return interpolation(sedarim, sequence, IdentityMatrix.of(sequence.length()));
  }

  /** @param sedarim
   * @param sequence
   * @param values vector or matrix
   * @param covariance
   * @return */
  public static Kriging of(Sedarim sedarim, Tensor sequence, Tensor values, Tensor covariance) {
    // symmetric distance matrix eq (3.7.13)
    Tensor vardst = Tensor.of(sequence.stream().map(sedarim::sunder));
    SymmetricMatrixQ.require(vardst);
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    Scalar one = Quantity.of(RealScalar.ONE, Unprotect.getUnitUnique(matrix));
    int n = matrix.length();
    Tensor rhs = Tensors.of(values.get(0).map(Scalar::zero));
    LagrangeMultiplier lagrangeMultiplier = //
        new LagrangeMultiplier(matrix, values, ConstantArray.of(one, 1, n), rhs);
    // System.out.println(Pretty.of(lagrangeMultiplier.matrix().map(Round._1)));
    Tensor inverse = PseudoInverse.of(lagrangeMultiplier.matrix());
    Tensor weights = inverse.dot(lagrangeMultiplier.b());
    return new Kriging(sedarim, one, weights, inverse);
  }

  // ---
  private final Sedarim sedarim;
  private final Scalar one;
  private final Tensor weights;
  private final Tensor inverse;

  private Kriging(Sedarim sedarim, Scalar one, Tensor weights, Tensor inverse) {
    this.sedarim = sedarim;
    this.one = one;
    this.weights = weights;
    this.inverse = inverse;
  }

  private Tensor vs(Tensor point) {
    return sedarim.sunder(point).append(one);
  }

  /** @param point
   * @return estimate at given point */
  public Tensor estimate(Tensor point) {
    return vs(point).dot(weights);
  }

  /** @param point
   * @return variance of estimate at given point */
  public Scalar variance(Tensor point) {
    Tensor vs = vs(point);
    return (Scalar) inverse.dot(vs).dot(vs);
  }
}
