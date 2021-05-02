// code by gjoel
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.hs.sn.SnMetric;
import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Abs;

/** @see SnMetric */
public enum So2Metric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Abs.FUNCTION.apply(So2.MOD.apply((Scalar) p.subtract(q)));
  }
}
