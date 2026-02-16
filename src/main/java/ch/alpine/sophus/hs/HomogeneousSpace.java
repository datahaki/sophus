// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.sophus.math.api.GeodesicSpace;
import ch.alpine.sophus.math.api.Manifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** homogeneous space
 * 
 * interface that extends the capabilities of vector log manifold
 * hs exponential provides the exp function to move from a point p
 * to a point q on the manifold, via the tangent vector at p
 * 
 * the biinvariant mean is unique and canonic */
public interface HomogeneousSpace extends GeodesicSpace, Manifold {
  /** @return parallel transport */
  HsTransport hsTransport();

  BiinvariantMean biinvariantMean();

  /** related to "involution"/"involutive automorphism"
   * 
   * @param p
   * @param q
   * @return Exp_p[-Log_p[q]] */
  // TODO SOPHUS find an elegant way to check if default matches override
  default Tensor flip(Tensor p, Tensor q) {
    Exponential exp_p = exponential(p);
    return exp_p.exp(exp_p.log(q).negate());
  }

  @Override
  default ScalarTensorFunction curve(Tensor p, Tensor q) {
    Exponential exp_p = exponential(p);
    Tensor v = exp_p.log(q);
    return scalar -> exp_p.exp(v.multiply(scalar));
  }
}
