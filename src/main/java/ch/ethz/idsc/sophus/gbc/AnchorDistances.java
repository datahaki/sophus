// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.HsProjection.Matrix;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.BiinvariantVector;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Hint: DO NOT USE AnchorDistances EXCEPT IN AnchorCoordinates !!! */
/* package */ class AnchorDistances implements WeightingInterface, Serializable {
  private final HsProjection hsProjection;

  /** @param vectorLogManifold */
  public AnchorDistances(VectorLogManifold vectorLogManifold) {
    this.hsProjection = new HsProjection(vectorLogManifold);
  }

  public BiinvariantVector biinvariantVector(Tensor sequence, Tensor point) {
    Matrix matrix = hsProjection.new Matrix(sequence, point);
    return new BiinvariantVector( //
        matrix.influence(), // influence matrix, or hat matrix
        Tensor.of(matrix.leverages().stream() // stream of leverages
            .map(Scalar.class::cast) // theory asserts that leverage is in [0, 1]
            .map(Clips.unit()) // but the numerics don't always reflect that
            .map(Sqrt.FUNCTION))); //
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return biinvariantVector(sequence, point).distances();
  }
}
