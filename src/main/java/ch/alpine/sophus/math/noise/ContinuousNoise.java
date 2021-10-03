// code by jph
package ch.alpine.sophus.math.noise;

import ch.alpine.tensor.api.TensorScalarFunction;

/** maps given tensor to a scalar noise value
 * result should depend continuously on input */
@FunctionalInterface
public interface ContinuousNoise extends TensorScalarFunction {
  // ---
}
