// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.stream.Stream;

import ch.ethz.idsc.sophus.hs.Mahalanobis;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** The evaluation of garden distances for a fixed set of landmarks is very efficient,
 * since the mahalanobis form at the landmarks can be precomputed.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see HarborDistances */
public enum GardenDistances {
  ;
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, Tensor sequence) {
    Mahalanobis[] array = sequence.stream() //
        .map(vectorLogManifold::logAt) //
        .map(tangentSpace -> new Mahalanobis(tangentSpace, sequence)) //
        .toArray(Mahalanobis[]::new);
    return point -> Tensor.of(Stream.of(array).map(mahalanobis -> mahalanobis.distance(point)));
  }
}
