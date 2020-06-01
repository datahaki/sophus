// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Frobenius;

/** biinvariant coordinate */
public class GrCoordinate extends HsProjection implements TensorUnaryOperator {
  private final Tensor sequence;
  private final Tensor grassmann;

  public GrCoordinate(FlattenLogManifold flattenLogManifold, Tensor sequence) {
    super(flattenLogManifold);
    this.sequence = sequence;
    grassmann = Tensor.of(sequence.stream().map(point -> projection(sequence, point)));
  }

  @Override // maps to weights
  public Tensor apply(Tensor point) {
    Tensor proj = projection(sequence, point);
    Tensor dist = Tensor.of(grassmann.stream().map(x -> Frobenius.between(x, proj)));
    return NormalizeTotal.FUNCTION.apply(dist.map(Scalar::reciprocal));
  }
}
