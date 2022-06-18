// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.gbc.LagrangeCoordinates;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

public abstract class BiinvariantBase implements Biinvariant, Serializable {
  protected final Manifold manifold;
  private final HsDesign hsDesign;

  public BiinvariantBase(Manifold manifold) {
    this.manifold = manifold;
    hsDesign = new HsDesign(manifold);
  }

  @Override
  public final HsDesign hsDesign() {
    return hsDesign;
  }

  @Override
  public final TensorUnaryOperator var_dist(ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(sequence);
    Objects.requireNonNull(variogram);
    return point -> tensorUnaryOperator.apply(point).map(variogram);
  }

  @Override
  public final TensorUnaryOperator weighting(ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = var_dist(variogram, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
  }

  @Override
  public TensorUnaryOperator lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = weighting(variogram, sequence);
    return point -> LagrangeCoordinates.of( //
        hsDesign().matrix(sequence, point), // TODO SOPHUS ALG levers are computed twice
        tensorUnaryOperator.apply(point)); // target
  }
}
