// code by jph
package ch.alpine.sophus.lie.pgl;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.jet.LinearFractionalTransform;
import ch.alpine.tensor.mat.Tolerance;

/** @see LinearFractionalTransform */
public class PGlGroup extends GlGroup {
  public static final PGlGroup INSTANCE = new PGlGroup();

  PGlGroup() {
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> {
      int n = matrix.length() - 1;
      return super.isPointQ().test(matrix) //
          && Tolerance.CHOP.isZero(matrix.Get(n, n).subtract(RealScalar.ONE));
    };
  }

  @Override
  protected Tensor protected_project(Tensor matrix) {
    // TODO SOPHUS protected_project also required in EXPONENTIAL
    int n = matrix.length() - 1;
    return matrix.divide(matrix.Get(n, n));
  }
}
