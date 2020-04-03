// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.gbc.HsProjection;
import ch.ethz.idsc.sophus.gbc.ProjectionInterface;
import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** @see AbsoluteDistances */
/* package */ class RelativeDistances implements PseudoDistances, Serializable {
  private static final Scalar ONE_NEGATE = RealScalar.of(-1.0);
  // ---
  private final ProjectionInterface projectionInterface;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;

  /** @param flattenLogManifold
   * @param variogram
   * @param sequence */
  public RelativeDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.projectionInterface = new HsProjection(flattenLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
  }

  @Override // from PseudoDistances
  public Tensor pseudoDistances(Tensor point) {
    AtomicInteger atomicInteger = new AtomicInteger();
    return Tensor.of(projectionInterface.projection(sequence, point).stream() //
        .map(row -> {
          row.set(ONE_NEGATE::add, atomicInteger.getAndIncrement());
          return row;
        }) //
        .map(Norm._2::ofVector) //
        .map(variogram));
  }
}
