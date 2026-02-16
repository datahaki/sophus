// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** distance between two points
 * 
 * norm of tangent vector */
public interface MetricManifold extends Manifold, TensorMetric {
  /** @param p
   * @return bilinear form from semi-riemannian metric at point p */
  BilinearForm bilinearForm(Tensor p);

  @Override
  default Scalar distance(Tensor p, Tensor q) {
    return bilinearForm(p).norm(exponential(p).log(q));
  }
}
