// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;

public class SlGroup extends GlGroup {
  public static final SlGroup INSTANCE = new SlGroup();

  protected SlGroup() {
    // ---
  }

  @Override
  public Exponential exponential0() {
    return SlExponential.INSTANCE;
  }

  @Override // from MemberQ
  public MemberQ isPointQ() {
    return matrix -> SquareMatrixQ.INSTANCE.test(matrix) //
        && Tolerance.CHOP.isClose(Det.of(matrix), RealScalar.ONE);
  }

  @Override
  public String toString() {
    return "SL";
  }
}
