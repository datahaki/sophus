// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se3InverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      Se3Group.INSTANCE, //
      m4x4 -> Flatten.of(Se3Exponential.INSTANCE.log(m4x4)), //
      InverseNorm.of(RnNorm.INSTANCE));

  public static Tensor norm(Tensor lever) {
    VectorQ.require(lever);
    Scalar scalar = lever.flatten(-1).map(Scalar.class::cast).map(AbsSquared.FUNCTION).reduce(Scalar::add).get();
    return Sqrt.FUNCTION.apply(scalar);
  }
}
