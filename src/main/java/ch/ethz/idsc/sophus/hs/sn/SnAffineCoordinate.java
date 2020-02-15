// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Affine coordinates created by n points in d-dimensional vector space
 * 
 * Affine coordinates are generalized barycentric coordinates and satisfy
 * partition of unity property
 * linear reproduction property
 * C^infinity
 * 
 * but do not satisfy Lagrange property
 * 
 * Reference:
 * "Affine generalized barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011
 * 
 * Quote: "Among all generalized barycentric coordinates with respect to a
 * scattered set of points, Waldron suggests to consider those with minimal
 * L2-norm, which are uniquely defined as the affine functions."
 * in Kai Hormann, N. Sukumar, 2017 */
public enum SnAffineCoordinate implements BarycentricCoordinate {
  INSTANCE;

  @Override // from BarycentricCoordinates
  public Tensor weights(Tensor sequence, Tensor point) {
    return of(sequence).apply(point);
  }

  /** @param sequence matrix with dimensions n x d
   * @return
   * @throws Exception if given sequence is empty */
  public static TensorUnaryOperator of(Tensor sequence) {
    return new Operator(sequence);
  }

  private static class Operator implements TensorUnaryOperator {
    private final Scalar _1_n;
    private final SnExp mean;
    private final Tensor pinv;

    private Operator(Tensor sequence) {
      _1_n = RationalScalar.of(1, sequence.length());
      mean = new SnExp(SnMean.INSTANCE.mean(sequence, ConstantArray.of(_1_n, sequence.length())));
      pinv = PseudoInverse.of(Tensor.of(sequence.stream().map(mean::log)));
    }

    @Override
    public Tensor apply(Tensor x) {
      return mean.log(x).dot(pinv).map(_1_n::add);
    }
  }
}
