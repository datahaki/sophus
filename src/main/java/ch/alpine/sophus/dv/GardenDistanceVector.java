// code by jph
package ch.alpine.sophus.dv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.gr.Mahalanobis;

/** The evaluation of garden distances for a fixed set of landmarks is very efficient,
 * since the {@link Mahalanobis} form at the landmarks can be precomputed.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see HarborBiinvariantVector */
public class GardenDistanceVector implements TensorUnaryOperator {
  /** @param manifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(Manifold manifold, Tensor sequence) {
    return new GardenDistanceVector(Objects.requireNonNull(manifold), sequence);
  }

  // ---
  private final List<Exponential> tangentSpaces;
  private final List<Mahalanobis> array;

  public GardenDistanceVector(Manifold manifold, Tensor sequence) {
    tangentSpaces = new ArrayList<>(sequence.length());
    array = new ArrayList<>(sequence.length());
    for (Tensor point : sequence) {
      Exponential exponential = manifold.exponential(point);
      tangentSpaces.add(exponential);
      array.add(new Mahalanobis(Tensor.of(sequence.stream().map(exponential::vectorLog))));
    }
  }

  @Override
  public Tensor apply(Tensor point) {
    AtomicInteger atomicInteger = new AtomicInteger();
    return Tensor.of(array.stream() //
        .map(mahalanobis -> mahalanobis.norm(tangentSpaces.get(atomicInteger.getAndIncrement()).vectorLog(point))));
  }
}
