// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.tensor.Scalar;

/** various norms for curve decimation */
public enum HsCurveDecimation {
  STANDARD() {
    @Override
    public CurveDecimation of(FlattenLogManifold flattenLogManifold, HsExponential hsExponential, Scalar epsilon) {
      HsLineDistance hsLineDistance = new HsLineDistance(flattenLogManifold);
      return new RamerDouglasPeucker(hsLineDistance, epsilon);
    }
  }, //
  MIDPOINT() {
    @Override
    public CurveDecimation of(FlattenLogManifold flattenLogManifold, HsExponential hsExponential, Scalar epsilon) {
      return new RamerDouglasPeucker(HsMidpointLineDistance.of(flattenLogManifold, hsExponential), epsilon);
    }
  }, //
  SYMMETRIZED() {
    @Override
    public CurveDecimation of(FlattenLogManifold flattenLogManifold, HsExponential hsExponential, Scalar epsilon) {
      HsLineDistance hsLineDistance = new HsLineDistance(flattenLogManifold);
      return new RamerDouglasPeucker(new SymmetricLineDistance(hsLineDistance), epsilon);
    }
  }, //
  PROJECTED() {
    @Override
    public CurveDecimation of(FlattenLogManifold flattenLogManifold, HsExponential hsExponential, Scalar epsilon) {
      return new RamerDouglasPeucker( //
          new HsProjectedLineDistance(hsExponential), //
          epsilon);
    }
  };

  /** @param flattenLogManifold
   * @param hsExponential
   * @param epsilon non-negative
   * @return */
  public abstract CurveDecimation of(FlattenLogManifold flattenLogManifold, HsExponential hsExponential, Scalar epsilon);
}
