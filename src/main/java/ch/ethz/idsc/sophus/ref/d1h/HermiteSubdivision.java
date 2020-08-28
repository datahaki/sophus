// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import ch.ethz.idsc.sophus.math.TensorIteration;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** assumes uniform sampling */
public interface HermiteSubdivision {
  /** @param delta between two samples in control points
   * @param control
   * @return */
  TensorIteration string(Scalar delta, Tensor control);

  /** @param delta between two samples in control points
   * @param control
   * @return */
  TensorIteration cyclic(Scalar delta, Tensor control);
}
