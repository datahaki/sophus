// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** TODO class design is not optimal!!! */
public class LeversCoordinate implements TensorUnaryOperator {
  private static final long serialVersionUID = -1903965539167868768L;
  private static final TensorUnaryOperator AFFINE = matrix -> ConstantArray.of(RealScalar.ONE, matrix.length());

  /** @param variogram
   * @return */
  public static TensorUnaryOperator of(ScalarUnaryOperator variogram) {
    return custom(new LeversWeighting(variogram));
  }

  /** @param target
   * @return */
  public static TensorUnaryOperator custom(TensorUnaryOperator target) {
    return new LeversCoordinate(Objects.requireNonNull(target));
  }

  /** @return */
  public static TensorUnaryOperator affine() {
    return custom(AFFINE);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  private LeversCoordinate(TensorUnaryOperator target) {
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor apply(Tensor levers) {
    return StaticHelper.barycentric( //
        target.apply(levers), // design matrix as input to target
        levers);
  }
}
