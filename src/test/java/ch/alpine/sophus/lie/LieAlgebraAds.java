// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.sophus.lie.sl.SlNGroup;
import ch.alpine.tensor.Tensor;

public enum LieAlgebraAds {
  ;
  public static Tensor se(int n) {
    Tensor basis = LieAlgebraMatrixBasis.of(new SeNGroup(n));
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    return matrixAlgebra.ad();
  }

  public static Tensor sl(int n) {
    Tensor basis = LieAlgebraMatrixBasis.of(new SlNGroup(n));
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    return matrixAlgebra.ad();
  }
}
