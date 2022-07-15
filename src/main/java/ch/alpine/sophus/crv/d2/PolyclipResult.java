// code by jph
package ch.alpine.sophus.crv.d2;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

public record PolyclipResult(Tensor tensor, Tensor belong) implements Serializable {
  public static final PolyclipResult EMPTY = new PolyclipResult( //
      Tensors.unmodifiableEmpty(), //
      Tensors.unmodifiableEmpty());
}
