// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/* package */ class RadialBasisFunctionImpl implements TensorUnaryOperator {
  private final TensorUnaryOperator normalize;
  private final PseudoDistances pseudoDistances;
  private final Tensor weights;

  public RadialBasisFunctionImpl(PseudoDistances pseudoDistances, TensorUnaryOperator normalize, Tensor sequence, Tensor values) {
    this.pseudoDistances = pseudoDistances;
    this.normalize = normalize;
    weights = LinearSolve.of(Tensor.of(sequence.stream().map(pseudoDistances::pseudoDistances).map(normalize)), values);
  }

  @Override
  public Tensor apply(Tensor point) {
    return normalize.apply(pseudoDistances.pseudoDistances(point)).dot(weights);
  }
}
