// code by jph
package ch.alpine.sophus.lie.se3;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.N;

public enum Se3Algebra implements LieAlgebra {
  INSTANCE;

  private final Tensor basis;
  private final Tensor ad;

  private Se3Algebra() {
    basis = Tensors.of( //
        Tensors.fromString("{{0, 0, 0, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, 0, 0}, {0, 0, -1, 0}, {0, +1, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, 0, +1, 0}, {0, 0, 0, 0}, {-1, 0, 0, 0}, {0, 0, 0, 0}}"), //
        Tensors.fromString("{{0, -1, 0, 0}, {+1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}"));
    ad = new MatrixAlgebra(basis).ad();
  }

  @Override // from LieAlgebra
  public Tensor ad() {
    return ad.copy();
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(N.DOUBLE.of(ad), degree);
  }

  @Override // from LieAlgebra
  public Tensor basis() {
    return basis.copy();
  }
}
