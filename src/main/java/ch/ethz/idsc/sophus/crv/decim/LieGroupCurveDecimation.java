// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;

/** various norms for curve decimation */
public enum LieGroupCurveDecimation {
  STANDARD() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential lieExponential, Scalar epsilon) {
      return new RamerDouglasPeucker(new LieGroupLineDistance(lieGroup, lieExponential::log), epsilon);
    }
  }, //
  MIDPOINT() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential lieExponential, Scalar epsilon) {
      return new RamerDouglasPeucker(new LieMidpointLineDistance(lieGroup, lieExponential), epsilon);
    }
  }, //
  SYMMETRIZED() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential lieExponential, Scalar epsilon) {
      return new RamerDouglasPeucker( //
          new SymmetricLineDistance(new LieGroupLineDistance(lieGroup, lieExponential::log)), //
          epsilon);
    }
  }, //
  PROJECT() {
    @Override
    public CurveDecimation of(LieGroup lieGroup, Exponential lieExponential, Scalar epsilon) {
      return new RamerDouglasPeucker( //
          new LieProjectedLineDistance(lieGroup, lieExponential), //
          epsilon);
    }
  };

  /** @param lieGroup
   * @param lieExponential
   * @param epsilon non-negative
   * @return */
  public abstract CurveDecimation of(LieGroup lieGroup, Exponential lieExponential, Scalar epsilon);
}
