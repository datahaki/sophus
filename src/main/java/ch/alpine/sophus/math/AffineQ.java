// code by ob, jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ConstraintMemberQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Total;

/** check if entries add up to one */
public class AffineQ extends ConstraintMemberQ {
  public static final ConstraintMemberQ INSTANCE = new AffineQ();

  private AffineQ() {
    super(1, Tolerance.CHOP);
  }

  @Override
  public Tensor constraint(Tensor tensor) {
    return Total.of(tensor).subtract(RealScalar.ONE);
  }
}
