// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Tensor;

public interface ClothoidInterface extends GeodesicInterface {
  @Override
  Clothoid curve(Tensor p, Tensor q);
}
