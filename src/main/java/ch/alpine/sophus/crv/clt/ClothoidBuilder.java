// code by jph
package ch.alpine.sophus.crv.clt;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.Tensor;

/** The constructed curve between the point p = {px, py, pa} and q = {qx, qy, qa} is of type {@link Clothoid} */
public interface ClothoidBuilder extends GeodesicSpace {
  @Override // from ParametricCurve
  Clothoid curve(Tensor p, Tensor q);
}
