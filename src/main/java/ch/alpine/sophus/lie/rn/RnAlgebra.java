// code by jph
package ch.alpine.sophus.lie.rn;

import java.io.Serializable;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.spa.SparseArray;

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

  @Override // from LieAlgebra
  public Tensor basis() {
    Tensor tensor = SparseArray.of(RealScalar.ZERO, n, n + 1, n + 1);
    IntStream.range(0, n).forEach(i -> tensor.set(RealScalar.ONE, i, i, n));
    return tensor;
  }
}
