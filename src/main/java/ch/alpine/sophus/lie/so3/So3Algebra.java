// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.sca.N;

public enum So3Algebra implements LieAlgebra {
  INSTANCE;

  private static final Tensor AD = LeviCivitaTensor.of(3).negate();

  @Override
  public Tensor ad() {
    return AD.copy();
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(N.DOUBLE.of(AD), degree);
  }

  @Override
  public Tensor basis() {
    return LeviCivitaTensor.of(3).negate();
  }
}
