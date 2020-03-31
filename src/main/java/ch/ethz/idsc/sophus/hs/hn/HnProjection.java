// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

public enum HnProjection implements TensorUnaryOperator {
  INSTANCE;

  @Override
  public Tensor apply(Tensor x) {
    Scalar xn2 = HnBilinearForm.between(x, x); // if x in H^n then equals to -1
    return x.divide(Sqrt.FUNCTION.apply(xn2.negate()));
  }
}
