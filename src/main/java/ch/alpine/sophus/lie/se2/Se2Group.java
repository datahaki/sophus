// code by jph
package ch.alpine.sophus.lie.se2;

import java.io.Serializable;

import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** parameterized by R^2 x [-pi, pi)
 * 
 * References:
 * http://vixra.org/abs/1807.0463
 * https://www.youtube.com/watch?v=2vDciaUgL4E
 * 
 * @see Se2Skew */
public class Se2Group extends AbstractSe2Group implements Serializable {
  public static final Se2Group INSTANCE = new Se2Group();

  private Se2Group() {
  }

  @Override // from LieGroup
  public Se2GroupElement element(Tensor xya) {
    return new Se2GroupElement(xya);
  }

  @Override // from GeodesicInterface
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SHOPHUS check is this impl adds value compared to default impl, perhaps due to Se2Integrator
    Tensor delta = new Se2GroupElement(p).inverse().combine(q);
    Tensor x = Se2CoveringGroup.INSTANCE.log(delta);
    return scalar -> Se2Integrator.INSTANCE.spin(p, x.multiply(scalar));
  }
}
