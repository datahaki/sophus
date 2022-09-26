// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

public abstract class BiinvariantBase implements Biinvariant, Serializable {
  protected final Manifold manifold;
  private final HsDesign hsDesign;

  protected BiinvariantBase(Manifold manifold) {
    this.manifold = manifold;
    hsDesign = new HsDesign(manifold);
  }

  @Override // from Biinvariant
  public final HsDesign hsDesign() {
    return hsDesign;
  }

  @Override // from Biinvariant
  public final Sedarim var_dist(ScalarUnaryOperator variogram, Tensor sequence) {
    Sedarim sedarim = distances(sequence);
    Objects.requireNonNull(variogram);
    return point -> sedarim.sunder(point).map(variogram);
  }

  @Override // from Biinvariant
  public final Sedarim weighting(ScalarUnaryOperator variogram, Tensor sequence) {
    Sedarim sedarim = var_dist(variogram, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(sedarim.sunder(point));
  }

  @Override // from Biinvariant
  public Sedarim lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    Sedarim sedarim = weighting(variogram, sequence);
    return point -> LagrangeCoordinates.of( //
        hsDesign().matrix(sequence, point), // TODO SOPHUS ALG levers are computed twice
        sedarim.sunder(point)); // target
  }
}
