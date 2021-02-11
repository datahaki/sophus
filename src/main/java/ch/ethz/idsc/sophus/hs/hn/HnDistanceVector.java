// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;

public enum HnDistanceVector implements Genesis {
  INSTANCE;

  @Override
  public Tensor origin(Tensor levers) {
    return Tensor.of(levers.stream().map(HnNorm.INSTANCE::ofVector));
  }
}
