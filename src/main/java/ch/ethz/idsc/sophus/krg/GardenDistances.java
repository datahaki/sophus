// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;
import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** @see HarborDistances */
public class GardenDistances implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new GardenDistances(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final VectorLogManifold vectorLogManifold;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor forms;

  private GardenDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.vectorLogManifold = vectorLogManifold;
    this.variogram = Objects.requireNonNull(variogram);
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
        .map(Sqrt.FUNCTION) //
        .map(variogram));
  }
}
