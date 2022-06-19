// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;

/** mixes units
 * 
 * ||log(p^-1.q)|| */
/* package */ enum Se2CoveringMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Vector2Norm.of(Se2CoveringGroup.INSTANCE.log( //
        new Se2CoveringGroupElement(p).inverse().combine(q)));
  }
}
