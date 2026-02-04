// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ConstraintSquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;

public class TSlMemberQ extends ConstraintSquareMatrixQ {
  public static final TSlMemberQ INSTANCE = new TSlMemberQ(Tolerance.CHOP);

  public TSlMemberQ(Chop chop) {
    super(chop);
  }

  @Override
  public Tensor defect(Tensor tensor) {
    return Trace.of(tensor);
  }
}
