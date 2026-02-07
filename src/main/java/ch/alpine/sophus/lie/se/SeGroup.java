// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.SquareMatrixQ;

public class SeGroup extends GlGroup {
  public static final SeGroup INSTANCE = new SeGroup();

  protected SeGroup() {
    // ---
  }

  @Override
  public boolean isMember(Tensor matrix) {
    int n = matrix.length();
    return SquareMatrixQ.INSTANCE.isMember(matrix) //
        && Last.of(matrix).equals(UnitVector.of(n, n - 1));
  }

  @Override
  public String toString() {
    return "SE";
  }
}
