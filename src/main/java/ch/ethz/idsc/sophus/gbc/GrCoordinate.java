// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** biinvariant coordinate */
public class GrCoordinate extends HsProjection implements TensorUnaryOperator {
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor grassmann;

  /** @param flattenLogManifold
   * @param variogram for instance power to two
   * @param sequence */
  public GrCoordinate(VectorLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    super(flattenLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
    grassmann = Tensor.of(sequence.stream().map(point -> projection(sequence, point)));
  }

  @Override // maps to weights
  public Tensor apply(Tensor point) {
    Tensor proj = projection(sequence, point);
    Tensor dist = Tensor.of(grassmann.stream() //
        .map(x -> Frobenius.between(x, proj)) //
        .map(variogram));
    return NormalizeTotal.FUNCTION.apply(dist.map(Scalar::reciprocal));
  }
}
