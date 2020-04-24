// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/* package */ class RadialBasisFunctionImpl implements TensorUnaryOperator {
  private final WeightingInterface weightingInterface;
  private final TensorUnaryOperator normalize;
  private final Tensor sequence;
  private final Tensor weights;

  public RadialBasisFunctionImpl(WeightingInterface weightingInterface, TensorUnaryOperator normalize, Tensor sequence, Tensor values) {
    this.weightingInterface = weightingInterface;
    this.normalize = normalize;
    this.sequence = sequence;
    weights = LinearSolve.of(Tensor.of(sequence.stream().map(point -> weightingInterface.weights(sequence, point)).map(normalize)), values);
  }

  @Override
  public Tensor apply(Tensor point) {
    return normalize.apply(weightingInterface.weights(sequence, point)).dot(weights);
  }
}
