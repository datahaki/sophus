// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeAffineGenesis implements Genesis, Serializable {
  private static final Genesis GENESIS = AffineCoordinate.INSTANCE;
  private static final int MAX_ITERATIONS = 128;
  // ---
  private final TensorUnaryOperator scalarUnaryOperator;
  private final Chop chop;

  public IterativeAffineGenesis(TensorUnaryOperator scalarUnaryOperator, Chop chop) {
    this.scalarUnaryOperator = Objects.requireNonNull(scalarUnaryOperator);
    this.chop = Objects.requireNonNull(chop);
  }

  private Tensor factors(Tensor levers) {
    Tensor factor = ConstantArray.of(RealScalar.ONE, levers.length());
    Tensor average = AveragingWeights.INSTANCE.origin(levers);
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor error = GENESIS.origin(factor.pmul(levers)).subtract(average);
      factor = factor.pmul(scalarUnaryOperator.apply(error));
      if (chop.allZero(error))
        return factor;
    }
    throw TensorRuntimeException.of(levers);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor factor = factors(levers);
    return NormalizeTotal.FUNCTION.apply(factor.pmul(GENESIS.origin(factor.pmul(levers))));
  }
}
