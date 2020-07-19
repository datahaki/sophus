// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.stream.Stream;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see HarborDistances */
public class GardenDistances implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new GardenDistances(vectorLogManifold, sequence);
  }

  /***************************************************/
  private final Mahalanobis[] mahalanobis;

  private GardenDistances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    mahalanobis = sequence.stream() //
        .map(vectorLogManifold::logAt) //
        .map(tangentSpace -> new Mahalanobis(tangentSpace, sequence)) //
        .toArray(Mahalanobis[]::new);
  }

  @Override
  public Tensor apply(Tensor point) {
    return Tensor.of(Stream.of(mahalanobis).map(m -> m.distance(point)));
  }
}
