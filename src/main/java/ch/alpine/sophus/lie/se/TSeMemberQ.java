package ch.alpine.sophus.lie.se;

import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.chq.ZeroDefectSquareMatrixQ;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

public class TSeMemberQ extends ZeroDefectSquareMatrixQ {
  private static final List<Integer> LIST = List.of(0, 0);
  public static final ZeroDefectArrayQ INSTANCE = new TSeMemberQ();

  private TSeMemberQ() {
    super(Chop._08);
  }

  @Override
  public Tensor defect(Tensor tensor) {
    int n = tensor.length() - 1;
    Tensor upper = tensor.block(LIST, List.of(n, n));
    return Join.of(AntisymmetricMatrixQ.INSTANCE.defect(upper), tensor.get(n));
  }
}
