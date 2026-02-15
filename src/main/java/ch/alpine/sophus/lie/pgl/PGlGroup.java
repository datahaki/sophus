// code by jph
package ch.alpine.sophus.lie.pgl;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.AbstractLieGroup;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.jet.LinearFractionalTransform;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.sca.Chop;

/** @see LinearFractionalTransform */
public class PGlGroup extends AbstractLieGroup {
  public static final PGlGroup INSTANCE = new PGlGroup();

  protected PGlGroup() {
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.reduce(this, Chop._10);
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> {
      int n = matrix.length() - 1;
      return GlGroup.INSTANCE.isPointQ().test(matrix) //
          && Tolerance.CHOP.isZero(matrix.Get(n, n).subtract(RealScalar.ONE));
    };
  }

  static Tensor protected_project(Tensor matrix) {
    int n = matrix.length() - 1;
    return matrix.divide(matrix.Get(n, n));
  }

  @Override
  public Exponential exponential0() {
    return PGlExponential.INSTANCE;
  }

  @Override
  public Tensor neutral(Tensor matrix) {
    return IdentityMatrix.of(matrix);
  }

  @Override
  public Tensor invert(Tensor matrix) {
    return protected_project(Inverse.of(matrix));
  }

  @Override
  public Tensor combine(Tensor matrix1, Tensor matrix2) {
    return protected_project(matrix1.dot(matrix2));
  }

  @Override
  public Tensor adjoint(Tensor g, Tensor v) { // v is square
    return dL(g, v).dot(invert(g));
  }

  @Override
  public Tensor dL(Tensor g, Tensor v) { // v is square
    return g.dot(SquareMatrixQ.INSTANCE.require(v));
  }

  @Override
  public String toString() {
    return "PGL";
  }
}
