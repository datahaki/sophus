// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

public enum Sl2Algebra implements LieAlgebra {
  INSTANCE;

  @Override
  public Tensor ad() {
    return Tensors.fromString( //
        "{{{0, 0, 0}, {0, 0, 1}, {0, -1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, -1, 0}, {1, 0, 0}, {0, 0, 0}}}");
  }

  public static Tensor basis() {
    return Tensors.of( //
        Tensors.fromString("{{0, 1}, {-1, 0}}"), //
        Tensors.fromString("{{0, 1}, {+1, 0}}"), //
        Tensors.fromString("{{1, 0}, {0, -1}}")).multiply(RationalScalar.HALF);
  }
}
