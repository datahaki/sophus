// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** parameterized by R^2 x [-pi, pi) */
public enum Se2Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public Se2GroupElement element(Tensor xya) {
    return new Se2GroupElement(xya);
  }

  @Override
  public Exponential exponential() {
    return Se2CoveringGroup.INSTANCE.exponential();
  }

  /** Hint:
   * The angular coordinate is not automatically mapped to [-pi, pi).
   * 
   * References:
   * http://vixra.org/abs/1807.0463
   * https://www.youtube.com/watch?v=2vDciaUgL4E */
  @Override // from GeodesicInterface
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SHOPHUS check is this impl adds value compared to default impl
    Tensor delta = new Se2GroupElement(p).inverse().combine(q);
    Tensor x = Se2CoveringGroup.INSTANCE.exponential().log(delta);
    return scalar -> Se2Integrator.INSTANCE.spin(p, x.multiply(scalar));
  }
}
