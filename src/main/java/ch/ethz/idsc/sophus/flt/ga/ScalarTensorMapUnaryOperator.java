// code by ob, jph
package ch.ethz.idsc.sophus.flt.ga;

import java.util.NavigableMap;
import java.util.function.UnaryOperator;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** operator that processes and returns a navigable map */
@FunctionalInterface
public interface ScalarTensorMapUnaryOperator extends UnaryOperator<NavigableMap<Scalar, Tensor>> {
  // ---
}
