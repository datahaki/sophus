// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;

/** various norms for curve decimation */
public enum HsCurveDecimation {
  STANDARD() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential exponential, Scalar epsilon) {
      HsLineDistance hsLineDistance = new HsLineDistance(LieFlattenLogManifold.of(lieGroup, exponential::log));
      return new RamerDouglasPeucker(hsLineDistance, epsilon);
    }
  }, //
  MIDPOINT() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential exponential, Scalar epsilon) {
      return new RamerDouglasPeucker(HsMidpointLineDistance.of(lieGroup, exponential), epsilon);
    }
  }, //
  SYMMETRIZED() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential exponential, Scalar epsilon) {
      HsLineDistance hsLineDistance = new HsLineDistance(LieFlattenLogManifold.of(lieGroup, exponential::log));
      return new RamerDouglasPeucker(new SymmetricLineDistance(hsLineDistance), epsilon);
    }
  }, //
  LIE_PROJECT() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential exponential, Scalar epsilon) {
      return new RamerDouglasPeucker( //
          new LieProjectedLineDistance(lieGroup, exponential), //
          epsilon);
    }
  };

  /** @param lieGroup
   * @param exponential
   * @param epsilon non-negative
   * @return */
  public abstract CurveDecimation of(LieGroup lieGroup, Exponential exponential, Scalar epsilon);
}