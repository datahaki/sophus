package ch.alpine.sophus.hs.s;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;

public class UnitVectorQ extends ZeroDefectArrayQ {
  public static final ZeroDefectArrayQ INSTANCE = new UnitVectorQ();

  private UnitVectorQ() {
    super(1, Tolerance.CHOP);
  }

  @Override
  public Tensor defect(Tensor p) {
    return Vector2Norm.of(p).subtract(RealScalar.ONE);
  }
}
