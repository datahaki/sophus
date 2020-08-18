// code by gjoel
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** length of clothoid from p to q in the 2-dimensional Euclidean plane
 * 
 * Careful: this distance is not symmetric as
 * the clothoid from p to q is generally different from
 * the clothoid from q to p! */
public enum ClothoidDistance implements TensorMetric, TensorNorm {
  SE2_ANALYTIC(ClothoidBuilders.SE2_ANALYTIC), //
  SE2_LEGENDRE(ClothoidBuilders.SE2_LEGENDRE), //
  SE2_COVERING(ClothoidBuilders.SE2_COVERING), //
  ;

  private final ClothoidBuilder clothoidBuilder;

  private ClothoidDistance(ClothoidBuilder clothoidBuilder) {
    this.clothoidBuilder = clothoidBuilder;
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return clothoidBuilder.curve(p, q).length();
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor xya) {
    return distance(xya.map(Scalar::zero), xya);
  }
}
