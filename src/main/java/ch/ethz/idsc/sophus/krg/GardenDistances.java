// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

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
  private final VectorLogManifold vectorLogManifold;
  private final Tensor sequence;
  private final Tensor forms;

  private GardenDistances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.vectorLogManifold = vectorLogManifold;
    this.sequence = sequence;
    Mahalanobis mahalanobis = new Mahalanobis(vectorLogManifold);
    forms = Tensor.of(sequence.stream() //
        .map(point -> mahalanobis.new Form(sequence, point)) //
        .map(Form::sigma_inverse));
  }

  @Override
  public Tensor apply(Tensor point) {
    return Tensor.of(IntStream.range(0, sequence.length()) //
        .mapToObj(index -> {
          Tensor v = vectorLogManifold.logAt(sequence.get(index)).vectorLog(point);
          return forms.get(index).dot(v).dot(v).Get();
        }) //
        .map(Sqrt.FUNCTION));
  }
}
