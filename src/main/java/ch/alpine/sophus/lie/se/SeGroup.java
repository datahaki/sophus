// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

public class SeGroup extends GlGroup {
  public static final SeGroup INSTANCE = new SeGroup();

  protected SeGroup() {
    // ---
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> {
      int n = matrix.length();
      return SquareMatrixQ.INSTANCE.test(matrix) //
          && Tolerance.CHOP.isClose(Last.of(matrix), UnitVector.of(n, n - 1));
    };
  }

  private enum Exponential0 implements Exponential {
    INSTANCE;

    @Override // from Exponential
    public Tensor exp(Tensor matrix) {
      return MatrixExp.of(matrix);
    }

    @Override // from Exponential
    public Tensor log(Tensor matrix) {
      return MatrixLog.of(matrix);
    }

    @Override
    public ZeroDefectArrayQ isTangentQ() {
      return TSeMemberQ.INSTANCE;
    }
  }

  @Override
  public Exponential exponential0() {
    return Exponential0.INSTANCE;
  }

  @Override
  public String toString() {
    return "SE";
  }
}
