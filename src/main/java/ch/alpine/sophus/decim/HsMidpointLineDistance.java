// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.decim.HsLineDistance.NormImpl;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.HsMidpoint;
import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

public record HsMidpointLineDistance(HsLineDistance hsLineDistance, HsMidpoint hsMidpoint) //
    implements LineDistance, Serializable {
  /** @param lieGroup
   * @param exponential
   * @return */
  public static LineDistance of(LieGroup lieGroup, Exponential exponential) {
    return of(LieExponential.of(lieGroup, exponential));
  }

  /** @param vectorLogManifold
   * @param hsManifold
   * @return */
  public static LineDistance of(HsManifold hsManifold) {
    return new HsMidpointLineDistance( //
        new HsLineDistance(hsManifold), //
        new HsMidpoint(hsManifold));
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    return hsLineDistance.tensorNorm(hsMidpoint.midpoint(p, q), q);
  }
}
