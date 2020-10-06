// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.Mahalanobis;
import ch.ethz.idsc.sophus.hs.TangentSpace;
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
    TangentSpace[] tangentSpaces = sequence.stream() //
        .map(vectorLogManifold::logAt) //
        .toArray(TangentSpace[]::new);
    Mahalanobis[] array = sequence.stream() // TODO not efficient since computes tangent space twice!
        .map(point -> new Mahalanobis(new HsDesign(vectorLogManifold).matrix(sequence, point))) //
        .toArray(Mahalanobis[]::new);
    return point -> Tensor.of(IntStream.range(0, sequence.length()) //
        .mapToObj(index -> array[index].distance(tangentSpaces[index].vectorLog(point))));
  }
}
