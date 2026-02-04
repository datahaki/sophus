package ch.alpine.sophus.lie.sp;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ConstraintSquareMatrixQ;
import ch.alpine.tensor.sca.Chop;

public class TSpMemberQ extends ConstraintSquareMatrixQ {
  private final Tensor omega;

  public TSpMemberQ(int n, Chop chop) {
    super(chop);
    omega = Symplectic.omega(n);
  }

  public TSpMemberQ(int n) {
    this(n, Chop._10);
  }

  @Override
  public Tensor defect(Tensor v) {
    return omega.dot(v).add(Transpose.of(v).dot(omega));
  }
}
