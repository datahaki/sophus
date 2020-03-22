// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.qty.Boole;

public class KrigingInterpolation implements TensorUnaryOperator {
  /** @param tensorNorm
   * @param sequence
   * @param values
   * @return */
  public static TensorUnaryOperator of(TensorNorm tensorNorm, Tensor sequence, Tensor values) {
    return new KrigingInterpolation(tensorNorm, sequence, values);
  }

  public static TensorUnaryOperator barycentric(TensorNorm tensorNorm, Tensor sequence) {
    return new KrigingInterpolation(tensorNorm, sequence, IdentityMatrix.of(sequence.length()));
  }

  /***************************************************/
  private final TensorNorm tensorNorm;
  private final Tensor sequence;
  private final Tensor weights;

  /** @param tensorNorm
   * @param sequence
   * @param values */
  private KrigingInterpolation(TensorNorm tensorNorm, Tensor sequence, Tensor values) {
    this.tensorNorm = tensorNorm;
    this.sequence = sequence;
    Tensor matrix = Tensor.of(sequence.stream().map(this::distances));
    int n = sequence.length();
    weights = LinearSolve.of( //
        matrix.append(Tensors.vector(i -> Boole.of(i < n), n + 1)), //
        values.copy().append(values.get(0).map(Scalar::zero)));
  }

  @Override
  public Tensor apply(Tensor x) {
    return distances(x).dot(weights);
  }

  private Tensor distances(Tensor x) {
    return Tensor.of(sequence.stream() //
        .map(x::subtract) //
        .map(tensorNorm::norm)) //
        .append(RealScalar.ONE);
  }
}
