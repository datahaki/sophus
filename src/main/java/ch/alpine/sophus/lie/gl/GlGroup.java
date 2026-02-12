// code by jph
package ch.alpine.sophus.lie.gl;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.sca.Chop;

/** Lie group GL(n) of invertible square matrices
 * also called "immersely linear Lie group"
 * 
 * Exp_e[X] and Log_e[M]
 * 
 * input X is a square matrix
 * input M is an invertible matrix */
public class GlGroup implements LieGroup, Serializable {
  public static final GlGroup INSTANCE = new GlGroup();

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.reduce(this, Chop._10);
  }

  @Override // from MemberQ
  public MemberQ isPointQ() {
    return matrix -> SquareMatrixQ.INSTANCE.test(matrix) //
        && !Tolerance.CHOP.isZero(Det.of(matrix));
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
      return SquareMatrixQ.INSTANCE;
    }
  }

  @Override
  public Exponential exponential0() {
    return Exponential0.INSTANCE;
  }

  @Override
  public final Tensor neutral(Tensor matrix) {
    return IdentityMatrix.of(matrix);
  }

  @Override
  public Tensor invert(Tensor matrix) {
    return protected_project(Inverse.of(matrix));
  }

  @Override
  public final Tensor combine(Tensor matrix1, Tensor matrix2) {
    return protected_project(matrix1.dot(SquareMatrixQ.INSTANCE.require(matrix2)));
  }

  protected Tensor protected_project(Tensor matrix) {
    return matrix;
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
    return "GL";
  }
}
