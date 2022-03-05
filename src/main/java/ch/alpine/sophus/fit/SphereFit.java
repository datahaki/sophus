// code by jph
package ch.alpine.sophus.fit;

import java.io.Serializable;
import java.util.Optional;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.mat.pi.LeastSquares;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.sca.pow.Sqrt;

/** reference: "Circle fitting by linear and non-linear least squares", by J. D. Coope */
public record SphereFit(Tensor center, Scalar radius) implements Serializable {
  /** @param points encoded as matrix
   * @return optional with instance of SphereFit containing the center and radius
   * of the fitted sphere, or empty if points are numerically co-linear
   * @throws Exception if points is empty, or not a matrix */
  public static Optional<SphereFit> of(Tensor points) {
    Tensor A = Tensor.of(points.stream().map(point -> point.add(point).append(RealScalar.ONE)));
    Tensor b = Tensor.of(points.stream().map(Vector2NormSquared::of));
    int cols = Unprotect.dimension1(A);
    if (A.length() < cols || //
        MatrixRank.of(A) < cols)
      return Optional.empty();
    Tensor x = LeastSquares.of(A, b);
    Tensor center = Tensor.of(x.stream().limit(cols - 1));
    return Optional.of(new SphereFit( //
        center, //
        Sqrt.FUNCTION.apply(x.Get(cols - 1).add(Vector2NormSquared.of(center)))));
  }
}
