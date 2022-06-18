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
 * partition of unity
 * linear reproduction
 * Lagrange
 * C^infinity (except at points from input set)
 * 
 * in general, the coordinates may evaluate to be negative
 * 
 * Lie affine coordinates are generalized barycentric coordinates for
 * scattered sets of points on a Lie-group with the properties:
 * 
 * coordinates sum up to 1
 * linear reproduction
 * Biinvariant: invariant under left-, right- and inverse action
 * 
 * However, generally NOT fulfilled:
 * Lagrange property
 * non-negativity
 * 
 * Log[g.m.g^-1] == Ad[g].Log[m]
 * Log[g.m] == Ad[g].Log[m.g]
 * Log[g^-1.m] == Ad[g^-1].Log[m.g^-1]
 * Ad[g].Log[g^-1.m] == Log[m.g^-1]
 * 
 * invariance under left-action is guaranteed because
 * log [(g x)^-1 g p] == log [x^-1 p]
 * 
 * If the target mapping is Ad invariant then invariance under right action
 * and inversion is guaranteed.
 * 
 * If the target mapping correlates to inverse distances then the coordinates
 * satisfy the Lagrange property.
 * 
 * References:
 * "Inverse Distance Coordinates for Scattered Sets of Points"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
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
    Objects.requireNonNull(variogram);
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
