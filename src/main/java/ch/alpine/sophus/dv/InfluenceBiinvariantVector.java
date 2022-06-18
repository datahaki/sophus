// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.sophus.hs.BiinvariantVector;
import ch.alpine.sophus.hs.BiinvariantVectorFunction;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;

/** for Rn and Sn the frobenius distance results in identical coordinates as the 2-norm distance
 * 
 * however, for SE(2) the frobenius and 2-norm coordinates do not match!
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public class InfluenceBiinvariantVector implements BiinvariantVectorFunction, Serializable {
  private final HsDesign hsDesign;
  private final Tensor sequence;
  private final TensorMetric tensorMetric;
  private final Tensor[] influence;

  /** @param manifold
   * @param sequence */
  public InfluenceBiinvariantVector(HsDesign hsDesign, Tensor sequence, TensorMetric tensorMetric) {
    this.hsDesign = hsDesign;
    this.sequence = sequence;
    this.tensorMetric = Objects.requireNonNull(tensorMetric);
    influence = sequence.stream() //
        .map(point -> hsDesign.matrix(sequence, point)) //
        .map(InfluenceMatrix::of) //
        .map(InfluenceMatrix::matrix) //
        .toArray(Tensor[]::new);
  }

  @Override
  public BiinvariantVector biinvariantVector(Tensor point) {
    Tensor design = hsDesign.matrix(sequence, point);
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(design);
    Tensor matrix = influenceMatrix.matrix();
    return new BiinvariantVector( //
        influenceMatrix, //
        Tensor.of(Stream.of(influence).map(x -> tensorMetric.distance(x, matrix))));
  }
}
