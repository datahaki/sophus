// code by jph
package ch.alpine.sophus.lie.se3;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.N;

public enum Se3Algebra implements LieAlgebra {
  INSTANCE;

  private final Tensor ad;

  Se3Algebra() {
    ad = new MatrixAlgebra(basis()).ad();
  }

  @Override // from LieAlgebra
  public Tensor ad() {
    return ad.copy();
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(ad.map(N.DOUBLE), degree);
  }

  public static Tensor basis() {
    return Tensors.of( //
        Tensors.fromString("{{0, 0, 0, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, 0, 0}, {0, 0, -1, 0}, {0, +1, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, +1, 0}, {0, 0, 0, 0}, {-1, 0, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, -1, 0, 0}, {+1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}"));
  }
}
