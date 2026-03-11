// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.sophus.lie.sl.SlNGroup;
import ch.alpine.tensor.Tensor;

public enum LieAlgebraAds {
  ;
  public static Tensor se(int n) {
    return new LieMatrixAlgebra(new SeNGroup(n)).ad();
  }

  public static Tensor sl(int n) {
    return new LieMatrixAlgebra(new SlNGroup(n)).ad();
  }
}
