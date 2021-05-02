// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.sophus.math.Geodesic;
import ch.alpine.tensor.Tensor;

/** The constructed curve between the point p = {px, py, pa} and q = {qx, qy, qa} is of type {@link Clothoid} */
public interface ClothoidBuilder extends Geodesic {
  @Override // from ParametricCurve
  Clothoid curve(Tensor p, Tensor q);
}
