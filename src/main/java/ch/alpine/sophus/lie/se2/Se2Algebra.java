// code by jph
package ch.alpine.sophus.lie.se2;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.N;

public enum Se2Algebra implements LieAlgebra {
  INSTANCE;

  private static final Tensor AD = Tensors.fromString( //
      "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}");

  @Override // from LieAlgebra
  public Tensor ad() {
    return AD.copy();
  }

  public static Tensor basis() {
    return Tensors.of( //
        Tensors.fromString("{{0,  0, 1}, {0, 0, 0}, {0, 0, 0}}"), //
        Tensors.fromString("{{0,  0, 0}, {0, 0, 1}, {0, 0, 0}}"), //
        Tensors.fromString("{{0, -1, 0}, {1, 0, 0}, {0, 0, 0}}"));
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(AD.map(N.DOUBLE), degree);
  }
}
