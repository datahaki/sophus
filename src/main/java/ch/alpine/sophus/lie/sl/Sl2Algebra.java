// code by jph
package ch.alpine.sophus.lie.sl;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.N;

public enum Sl2Algebra implements LieAlgebra {
  INSTANCE;

  @Override
  public Tensor ad() {
    return Tensors.fromString( //
        "{{{0, 0, 0}, {0, 0, 1}, {0, -1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, -1, 0}, {1, 0, 0}, {0, 0, 0}}}");
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(N.DOUBLE.of(ad()), degree);
  }

  public static Tensor basis() {
    return Tensors.of( //
        Tensors.fromString("{{0, 1}, {-1, 0}}"), //
        Tensors.fromString("{{0, 1}, {+1, 0}}"), //
        Tensors.fromString("{{1, 0}, {0, -1}}")).multiply(RationalScalar.HALF);
  }
}
