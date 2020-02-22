// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.lie.se2.Se2Skew;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.BiinvariantInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se2CoveringInverseDistanceCoordinate {
  ;
  private static final Tensor NEUTRAL = Tensors.vector(0, 0, 0);
  public static final BarycentricCoordinate INSTANCE = new BiinvariantInverseDistanceCoordinate( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringExponential.INSTANCE::log, //
      NEUTRAL, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = new BiinvariantInverseDistanceCoordinate( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringExponential.INSTANCE::log, //
      NEUTRAL, //
      InverseNorm.of(RnNormSquared.INSTANCE));

  public static Tensor equation(Tensor xya) {
    return Se2Skew.of(xya, RealScalar.ONE).rhs().append(xya.Get(2)); // append biinvariant mean of angles
  }

  public static Tensor equation2(Tensor xya) {
    return Se2Skew.of(xya, RealScalar.of(2)).rhs().append(xya.Get(2)); // append biinvariant mean of angles
  }
}
