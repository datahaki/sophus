// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.math.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.N;

public class LieAlgebraImpl implements LieAlgebra {
  private final Tensor ad;
  private final boolean isNilpotent;

  public LieAlgebraImpl(Tensor ad) {
    ExactTensorQ.require(ad);
    this.ad = JacobiIdentity.require(ad);
    isNilpotent = NilpotentAlgebraQ.of(ad);
  }

  @Override
  public Tensor ad() {
    return ad;
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(isNilpotent ? ad : N.DOUBLE.of(ad), degree);
  }

  @Override
  public Tensor basis() {
    throw new UnsupportedOperationException();
  }
}
