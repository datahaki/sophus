// code by jph
package ch.alpine.sophus.lie.se2;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.N;

public enum Se2Algebra implements LieAlgebra {
  INSTANCE;

  private static final Tensor AD = Tensors.fromString( //
      "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}");

  @Override // from LieAlgebra
  public Tensor ad() {
    return AD.copy();
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(N.DOUBLE.of(AD), degree);
  }
}
