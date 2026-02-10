// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Total;

/** row stochastic matrices */
public class StochasticMatrixQ extends ZeroDefectArrayQ {
  public static final ZeroDefectArrayQ INSTANCE = new StochasticMatrixQ();

  private StochasticMatrixQ() {
    super(2, Tolerance.CHOP);
  }

  @Override // from ZeroDefectArrayQ
  public Tensor defect(Tensor tensor) {
    return Tensor.of(tensor.stream().map(row -> Total.of(row).subtract(RealScalar.ONE)));
  }
}
