// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.math.Geodesic;
import ch.ethz.idsc.tensor.Tensor;

/** The constructed curve between the point p = {px, py, pa} and q = {qx, qy, qa} is of type {@link Clothoid} */
public interface ClothoidBuilder extends Geodesic {
  @Override // from ParametricCurve
  Clothoid curve(Tensor p, Tensor q);
}
