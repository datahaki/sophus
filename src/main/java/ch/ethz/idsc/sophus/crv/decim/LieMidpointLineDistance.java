// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;

import ch.ethz.idsc.sophus.crv.decim.LieGroupLineDistance.NormImpl;
import ch.ethz.idsc.sophus.hs.HsMidpoint;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.PointLieExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public class LieMidpointLineDistance implements LineDistance, Serializable {
  private final HsMidpoint lieMidpointInterface;
  private final LieGroupLineDistance lieGroupLineDistance;

  public LieMidpointLineDistance(LieGroup lieGroup, Exponential lieExponential) {
    lieMidpointInterface = new HsMidpoint(PointLieExponential.of(lieGroup, lieExponential));
    lieGroupLineDistance = new LieGroupLineDistance(lieGroup, lieExponential::log);
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    return lieGroupLineDistance.tensorNorm(lieMidpointInterface.midpoint(p, q), q);
  }
}
