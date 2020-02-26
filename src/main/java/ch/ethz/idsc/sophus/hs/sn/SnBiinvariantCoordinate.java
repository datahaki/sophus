// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class SnBiinvariantCoordinate extends HsBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SnBiinvariantCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new SnBiinvariantCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  /***************************************************/
  public SnBiinvariantCoordinate(TensorUnaryOperator target) {
    super(target);
  }

  @Override
  public FlattenLog logAt(Tensor point) {
    return new SnExp(point);
  }
}
