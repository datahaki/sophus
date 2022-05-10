// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector1Norm;

public enum ScMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Vector1Norm.of(ScGroup.INSTANCE.log(q.divide(p.Get(0))));
  }
}
