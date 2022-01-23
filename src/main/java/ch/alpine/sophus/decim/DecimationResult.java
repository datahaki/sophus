// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/** @return points in the decimated sequence
 * vector with length of the original sequence */
public record DecimationResult(Tensor result, Tensor errors) implements Serializable {
  public static final DecimationResult EMPTY = new DecimationResult( //
      Tensors.unmodifiableEmpty(), //
      Tensors.unmodifiableEmpty());
}