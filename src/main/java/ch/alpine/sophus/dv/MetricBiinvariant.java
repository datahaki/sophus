// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.hs.gr.GrExponential;
import ch.alpine.sophus.hs.spd.SpdExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** left-invariant (biinvariant only if a biinvariant metric exists)
 * results in a symmetric distance matrix -> can use for kriging
 * 
 * the basis of the tangent space is chosen so that the metric restricted to the
 * tangent space is the bilinear form equals to the identity matrix.
 * 
 * Careful: Shepard interpolation does not reproduce linear functions.
 * (Because the inverse norms are not projected to the proper subspace.)
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation
 * 
 * for alternative implementations
 * 
 * @see HnMetricBiinvariant */
public class MetricBiinvariant extends BiinvariantBase {
  /** Careful: not suitable for {@link SpdExponential}, and {@link GrExponential}
   * because these implementations drop coefficients of the log in the vectorLog
   * implementation. that means the scalar product on the subspace would have to be
   * adapted. */
  private final TensorNorm tensorNorm;

  /** @param manifold that is instance of {@link TensorNorm} */
  public MetricBiinvariant(Manifold manifold) {
    super(manifold);
    tensorNorm = (TensorNorm) manifold;
  }

  @Override // from Biinvariant
  public Sedarim distances(Tensor sequence) {
    Objects.requireNonNull(sequence);
    return point -> Tensor.of(hsDesign().stream(sequence, point).map(tensorNorm::norm));
  }

  /** Inverse Distance Weighting does not reproduce linear functions in general. Therefore,
   * Inverse distance weights <b>do not</b> fall in the category of generalized barycentric
   * coordinates.
   * 
   * <p>Reference:
   * "A two-dimensional interpolation function for irregularly-spaced data"
   * by Donald Shepard, 1968 */
  public Genesis weighting(ScalarUnaryOperator variogram) {
    Objects.requireNonNull(variogram);
    // the normalization is necessary to compensate for division by zero
    return levers -> NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(tensorNorm::norm) //
        .map(variogram)));
  }

  @Override // from Biinvariant
  public Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(sequence);
    Objects.requireNonNull(variogram);
    return point -> coordinate(variogram).origin(hsDesign().matrix(sequence, point));
  }

  public Genesis coordinate(ScalarUnaryOperator variogram) {
    return levers -> StaticHelper.barycentric(levers, weighting(variogram).origin(levers));
  }

  @Override // from Biinvariant
  public Sedarim lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = hsDesign().matrix(sequence, point);
      return LagrangeCoordinates.of(levers, weighting(variogram).origin(levers));
    };
  }
}
