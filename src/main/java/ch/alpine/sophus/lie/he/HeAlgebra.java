// code by jph
package ch.alpine.sophus.lie.he;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;

public class HeAlgebra implements LieAlgebra, Serializable {
  private final MatrixAlgebra matrixAlgebra;

  public HeAlgebra(int n) {
    matrixAlgebra = new MatrixAlgebra(basis(n));
  }

  @Override
  public Tensor ad() {
    return matrixAlgebra.ad();
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(ad(), degree);
  }

  public static Tensor basis(int n) {
    Integers.requirePositive(n);
    Tensor tensor = Array.sparse(2 * n + 1, n + 2, n + 2);
    int index = -1;
    for (int i = 1; i <= n; ++i)
      tensor.set(RealScalar.ONE, ++index, 0, i);
    tensor.set(RealScalar.ONE, ++index, 0, n + 1);
    for (int i = 1; i <= n; ++i)
      tensor.set(RealScalar.ONE, ++index, i, n + 1);
    return tensor;
  }
}
