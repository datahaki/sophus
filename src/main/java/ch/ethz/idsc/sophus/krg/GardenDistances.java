// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
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
  private final TangentSpace[] tangentSpaces;
  private final Form[] forms;

  private GardenDistances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    tangentSpaces = sequence.stream() //
        .map(vectorLogManifold::logAt) //
        .toArray(TangentSpace[]::new);
    Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
    forms = sequence.stream() //
        .map(point -> mahalanobis.new Form(sequence, point)) //
        .toArray(Form[]::new);
  }

  @Override
  public Tensor apply(Tensor point) {
    AtomicInteger atomicInteger = new AtomicInteger();
    return Tensor.of(Stream.of(forms) //
        .map(form -> form.distance(tangentSpaces[atomicInteger.getAndIncrement()].vectorLog(point))));
  }
}
