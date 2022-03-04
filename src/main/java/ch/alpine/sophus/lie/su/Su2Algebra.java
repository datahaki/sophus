// code by jph
package ch.alpine.sophus.lie.su;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.ad.BakerCampbellHausdorff;
import ch.alpine.sophus.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/** https://en.wikipedia.org/wiki/Electroweak_interaction */
public enum Su2Algebra implements LieAlgebra {
  INSTANCE;

  private final MatrixAlgebra matrixAlgebra;

  private Su2Algebra() {
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
    // TODO state reference
    Tensor u1 = Tensors.fromString("{{0, I}, {I, 0}}");
    Tensor u2 = Tensors.fromString("{{0, -1}, {1, 0}}");
    Tensor u3 = Tensors.fromString("{{I, 0}, {0, -I}}");
    return Tensors.of(u1, u2, u3);
  }
}
