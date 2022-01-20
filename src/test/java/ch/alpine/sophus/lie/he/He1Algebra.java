// code by jph
package ch.alpine.sophus.lie.he;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;

public enum He1Algebra implements LieAlgebra {
  INSTANCE;

  private final MatrixAlgebra matrixAlgebra;

  private He1Algebra() {
    matrixAlgebra = new MatrixAlgebra(basis());
  }

  @Override
  public Tensor ad() {
    return matrixAlgebra.ad();
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(ad(), degree);
  }

  @Override
  public Tensor basis() {
    return Tensors.of( //
        Tensors.fromString("{{0,1,0}, {0,0,0}, {0,0,0}}"), //
        Tensors.fromString("{{0,0,1}, {0,0,0}, {0,0,0}}"), //
        Tensors.fromString("{{0,0,0}, {0,0,1}, {0,0,0}}"));
  }
}
