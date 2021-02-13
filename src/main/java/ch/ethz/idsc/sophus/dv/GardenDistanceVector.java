// code by jph
package ch.ethz.idsc.sophus.dv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.Mahalanobis;

/** The evaluation of garden distances for a fixed set of landmarks is very efficient,
 * since the {@link Mahalanobis} form at the landmarks can be precomputed.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see HarborBiinvariantVector */
public class GardenDistanceVector implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new GardenDistanceVector(Objects.requireNonNull(vectorLogManifold), sequence);
  }

  /***************************************************/
  private final List<TangentSpace> tangentSpaces;
  private final List<Mahalanobis> array;

  public GardenDistanceVector(VectorLogManifold vectorLogManifold, Tensor sequence) {
    tangentSpaces = new ArrayList<>(sequence.length());
    array = new ArrayList<>(sequence.length());
    for (Tensor point : sequence) {
      TangentSpace tangentSpace = vectorLogManifold.logAt(point);
      tangentSpaces.add(tangentSpace);
      array.add(new Mahalanobis(Tensor.of(sequence.stream().map(tangentSpace::vectorLog))));
    }
  }

  @Override
  public Tensor apply(Tensor point) {
    AtomicInteger atomicInteger = new AtomicInteger();
    return Tensor.of(array.stream() //
        .map(mahalanobis -> mahalanobis.norm(tangentSpaces.get(atomicInteger.getAndIncrement()).vectorLog(point))));
  }
}
