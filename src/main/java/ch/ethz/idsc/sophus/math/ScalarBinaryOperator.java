// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.Scalar;

public interface ScalarBinaryOperator extends BinaryOperator<Scalar>, Serializable {
  // ---
}
