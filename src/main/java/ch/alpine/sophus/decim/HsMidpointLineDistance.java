// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.decim.HsLineDistance.NormImpl;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

// TODO SOPHUS is this still useful?
public record HsMidpointLineDistance(HsLineDistance hsLineDistance, GeodesicSpace hsMidpoint) //
    implements LineDistance, Serializable {
  /** @param lieGroup
   * @return */
  public static LineDistance of(LieGroup lieGroup) {
    return of(lieGroup);
  }

  /** @param homogeneousSpace
   * @return */
  public static LineDistance of(HomogeneousSpace homogeneousSpace) {
    return new HsMidpointLineDistance( //
        new HsLineDistance(homogeneousSpace), //
        homogeneousSpace);
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    return hsLineDistance.tensorNorm(hsMidpoint.midpoint(p, q), q);
  }
}
