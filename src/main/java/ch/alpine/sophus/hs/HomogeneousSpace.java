// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;

/** homogeneous space
 * 
 * interface that extends the capabilities of vector log manifold
 * hs exponential provides the exp function to move from a point p
 * to a point q on the manifold, via the tangent vector at p */
public interface HomogeneousSpace extends GeodesicSpace, Manifold {
  /** @return parallel transport */
  HsTransport hsTransport();

  /** @param chop
   * @return */
  BiinvariantMean biinvariantMean(Chop chop);

  /** related to "involution"/"involutive automorphism"
   * 
   * @param p
   * @param q
   * @return Exp_p[-Log_p[q]] */
  default Tensor flip(Tensor p, Tensor q) {
    return exponential(p).flip(q);
  }

  @Override
  default ScalarTensorFunction curve(Tensor p, Tensor q) {
    Exponential exponential = exponential(p);
    Tensor log = exponential.log(q);
    return scalar -> exponential.exp(log.multiply(scalar));
  }

  @Override // from MidpointInterface
  default Tensor midpoint(Tensor p, Tensor q) {
    return exponential(p).midpoint(q);
  }
}
