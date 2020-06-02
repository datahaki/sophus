// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;

import ch.ethz.idsc.sophus.crv.decim.HsLineDistance.NormImpl;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.HsMidpoint;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public class HsMidpointLineDistance implements LineDistance, Serializable {
  /** @param lieGroup
   * @param exponential
   * @return */
  public static LineDistance of(LieGroup lieGroup, Exponential exponential) {
    // TODO flatten log instead of log!
    return of(LieVectorLogManifold.of(lieGroup, exponential::log), LieExponential.of(lieGroup, exponential));
  }

  /** @param vectorLogManifold
   * @param hsExponential
   * @return */
  public static LineDistance of(VectorLogManifold vectorLogManifold, HsExponential hsExponential) {
    return new HsMidpointLineDistance( //
        new HsLineDistance(vectorLogManifold), //
        new HsMidpoint(hsExponential));
  }

  /***************************************************/
  private final HsLineDistance hsLineDistance;
  private final HsMidpoint hsMidpoint;

  private HsMidpointLineDistance(HsLineDistance hsLineDistance, HsMidpoint hsMidpoint) {
    this.hsLineDistance = hsLineDistance;
    this.hsMidpoint = hsMidpoint;
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    return hsLineDistance.tensorNorm(hsMidpoint.midpoint(p, q), q);
  }
}
