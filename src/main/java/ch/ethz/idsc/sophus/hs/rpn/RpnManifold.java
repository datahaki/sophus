// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "Eichfeldtheorie" by Helga Baum, 2005, p. 22 */
public enum RpnManifold implements HsManifold {
  INSTANCE;

  @Override // from HsManifold
  public Exponential exponential(Tensor point) {
    return new RpnExponential(point);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new RpnExponential(point);
  }
}
