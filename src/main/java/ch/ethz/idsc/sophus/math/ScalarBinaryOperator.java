// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.Scalar;

@FunctionalInterface
public interface ScalarBinaryOperator extends BinaryOperator<Scalar>, Serializable {
  // ---
}
