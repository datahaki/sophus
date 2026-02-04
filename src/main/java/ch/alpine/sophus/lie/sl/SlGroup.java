package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;

public class SlGroup extends GlGroup {
  public static final SlGroup INSTANCE = new SlGroup();

  protected SlGroup() {
    // ---
  }

  @Override // from MemberQ
  public boolean isMember(Tensor matrix) {
    return SquareMatrixQ.INSTANCE.isMember(matrix) //
        && Tolerance.CHOP.isClose(Det.of(matrix), RealScalar.ONE);
  }

  @Override
  public String toString() {
    return "SL";
  }
}
