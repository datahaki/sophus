// code by jph
package ch.alpine.sophus.lie.rn;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

public class RnAlgebra implements LieAlgebra, Serializable {
  private final int n;

  public RnAlgebra(int n) {
    this.n = n;
  }

  @Override // from LieAlgebra
  public Tensor ad() {
    return ConstantArray.of(RealScalar.ZERO, n, n, n);
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return Tensor::add;
  }
}
