// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;
import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** @see CompleteDistances */
public class Mahalanobis2Distances implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new Mahalanobis2Distances(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final VectorLogManifold vectorLogManifold;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor forms;

  private Mahalanobis2Distances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.vectorLogManifold = vectorLogManifold;
    this.variogram = Objects.requireNonNull(variogram);
    this.sequence = sequence;
    Mahalanobis mahalanobisForm = new Mahalanobis(vectorLogManifold);
    forms = Tensor.of(sequence.stream().map(point -> mahalanobisForm.new Form(sequence, point).sigma_inverse()));
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
