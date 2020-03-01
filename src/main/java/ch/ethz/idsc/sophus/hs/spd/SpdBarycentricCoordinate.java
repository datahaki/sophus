// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class SpdBarycentricCoordinate extends HsBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SpdBarycentricCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new SpdBarycentricCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  public SpdBarycentricCoordinate(TensorUnaryOperator target) {
    super(target);
  }

  @Override
  public FlattenLog logAt(Tensor point) {
    return new SpdExp(point);
  }
}
