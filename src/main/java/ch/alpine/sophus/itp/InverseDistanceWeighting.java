// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** Inverse Distance Weighting does not reproduce linear functions in general. Therefore,
 * Inverse distance weights <b>do not</b> fall in the category of generalized barycentric
 * coordinates.
 * 
 * <p>Reference:
 * "A two-dimensional interpolation function for irregularly-spaced data"
 * by Donald Shepard, 1968 */
// TODO SOPHUS API this can be achieved via Biinvarint
public record InverseDistanceWeighting(ScalarUnaryOperator variogram, TensorNorm tensorScalarFunction) //
    implements Genesis, Serializable {
  public InverseDistanceWeighting {
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(tensorScalarFunction);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(tensorScalarFunction::norm) //
        .map(variogram)));
  }
}
