// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.gbc.HsProjection;
import ch.ethz.idsc.sophus.gbc.ProjectionInterface;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** @see AbsoluteDistances */
/* package */ class RelativeDistances implements WeightingInterface, Serializable {
  private static final Scalar ONE_NEGATE = RealScalar.of(-1.0);
  // ---
  private final ProjectionInterface projectionInterface;
  private final ScalarUnaryOperator variogram;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence */
  public RelativeDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    this.projectionInterface = new HsProjection(vectorLogManifold);
    this.variogram = variogram;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
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
