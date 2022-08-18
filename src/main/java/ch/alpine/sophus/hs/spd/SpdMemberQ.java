// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.red.Max;

public enum SpdMemberQ implements MemberQ {
  INSTANCE;

  private static final ScalarUnaryOperator MAX = Max.function(RealScalar.of(1e-12));

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return SymmetricMatrixQ.of(p) //
        && PositiveDefiniteMatrixQ.ofHermitian(p);
  }

  public static Tensor project(Tensor x) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(Symmetrize.of(x));
    Tensor vector = eigensystem.values().map(MAX);
    return BasisTransform.ofMatrix(DiagonalMatrix.with(vector), eigensystem.vectors());
  }
}
