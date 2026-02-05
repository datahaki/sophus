package ch.alpine.sophus.lie.pgl;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.chq.ZeroDefectSquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;

public class TPGlMemberQ extends ZeroDefectSquareMatrixQ {
  public static final ZeroDefectArrayQ INSTANCE = new TPGlMemberQ(Tolerance.CHOP);

  public TPGlMemberQ(Chop chop) {
    super(chop);
  }

  @Override
  public Tensor defect(Tensor tensor) {
    int n = tensor.length() - 1;
    Scalar last = tensor.Get(n, n);
    return last.subtract(last.one());
  }
}
