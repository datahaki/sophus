// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class HsProjection implements ProjectionInterface, Serializable {
  protected final VectorLogManifold vectorLogManifold;

  /** @param vectorLogManifold non-null */
  public HsProjection(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
  }

  public final Tensor levers(Tensor sequence, Tensor point) {
    return Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog));
  }

  @Override // from ProjectionInterface
  public final Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = levers(sequence, point);
    Tensor nullsp = LeftNullSpace.usingQR(levers);
    return Transpose.of(nullsp).dot(nullsp);
  }
}
