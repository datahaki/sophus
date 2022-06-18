// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.gbc.LagrangeCoordinates;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.HsGenesis;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.gr.GrExponential;
import ch.alpine.sophus.hs.spd.SpdExponential;
import ch.alpine.sophus.itp.InverseDistanceWeighting;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
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
   * adapted.
   * 
   * scalar product has diagonal of all ones, i.e. [1, 1, ..., 1] */
  // public static final Biinvariant EUCLIDEAN = new MetricBiinvariant(Vector2Norm::of);
  // /** for {@link SpdExponential}, and {@link GrExponential} */
  // public static final Biinvariant VECTORIZE0 = new MetricBiinvariant(LowerVectorize0_2Norm.INSTANCE::norm);
  private final TensorNorm tensorNorm;

  /** @param tensorScalarFunction norm */
  public MetricBiinvariant(Manifold manifold) {
    super(manifold);
    tensorNorm = (TensorNorm) manifold;
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(Tensor sequence) {
    Objects.requireNonNull(manifold);
    Objects.requireNonNull(sequence);
    return point -> Tensor.of(new HsDesign(manifold).stream(sequence, point) //
        .map(tensorNorm::norm));
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    Genesis genesis = new MetricCoordinate(new InverseDistanceWeighting(variogram, tensorNorm::norm));
    return HsGenesis.wrap(hsDesign(), genesis, sequence);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(manifold);
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = hsDesign().matrix(sequence, point);
      return LagrangeCoordinates.of( //
          levers, //
          NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
              .map(tensorNorm::norm) //
              .map(variogram))));
    };
  }
}
