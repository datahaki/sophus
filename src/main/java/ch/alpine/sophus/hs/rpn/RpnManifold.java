// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.tensor.Tensor;

/** Reference:
 * "Eichfeldtheorie" by Helga Baum, 2005, p. 22 */
public enum RpnManifold implements HsManifold {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new RpnExponential(point);
  }

  @Override // from HsManifold
  public Exponential exponential(Tensor point) {
    return new RpnExponential(point);
  }
}
