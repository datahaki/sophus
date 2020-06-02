// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Scalar;

/** various norms for curve decimation */
public enum HsCurveDecimation {
  STANDARD() {
    @Override
    public CurveDecimation of(VectorLogManifold vectorLogManifold, HsExponential hsExponential, Scalar epsilon) {
      HsLineDistance hsLineDistance = new HsLineDistance(vectorLogManifold);
      return new RamerDouglasPeucker(hsLineDistance, epsilon);
    }
  }, //
  MIDPOINT() {
    @Override
    public CurveDecimation of(VectorLogManifold vectorLogManifold, HsExponential hsExponential, Scalar epsilon) {
      return new RamerDouglasPeucker(HsMidpointLineDistance.of(vectorLogManifold, hsExponential), epsilon);
    }
  }, //
  SYMMETRIZED() {
    @Override
    public CurveDecimation of(VectorLogManifold vectorLogManifold, HsExponential hsExponential, Scalar epsilon) {
      HsLineDistance hsLineDistance = new HsLineDistance(vectorLogManifold);
      return new RamerDouglasPeucker(new SymmetricLineDistance(hsLineDistance), epsilon);
    }
  }, //
  PROJECTED() {
    @Override
    public CurveDecimation of(VectorLogManifold vectorLogManifold, HsExponential hsExponential, Scalar epsilon) {
      return new RamerDouglasPeucker( //
          new HsProjectedLineDistance(hsExponential), //
          epsilon);
    }
  };

  /** @param vectorLogManifold
   * @param hsExponential
   * @param epsilon non-negative
   * @return */
  public abstract CurveDecimation of(VectorLogManifold vectorLogManifold, HsExponential hsExponential, Scalar epsilon);
}
