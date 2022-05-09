// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** interface that extends the capabilities of vector log manifold
 * hs exponential provides the exp function to move from a point p
 * to a point q on the manifold, via the tangent vector at p */
public interface HsManifold extends VectorLogManifold, GeodesicSpace {
  /** @param p
   * @return exponential map at given point p on manifold */
  Exponential exponential(Tensor p);

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
    return scalar -> split(p, q, scalar);
  }

  @Override
  default Tensor split(Tensor p, Tensor q, Scalar scalar) {
    Exponential exponential = exponential(p);
    return exponential.exp(exponential.log(q).multiply(scalar));
  }

  @Override // from MidpointInterface
  default Tensor midpoint(Tensor p, Tensor q) {
    return exponential(p).midpoint(q);
  }
}
