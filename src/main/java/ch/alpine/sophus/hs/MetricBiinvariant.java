// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.gbc.LagrangeCoordinates;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.hs.gr.GrExponential;
import ch.alpine.sophus.hs.hn.HnMetricBiinvariant;
import ch.alpine.sophus.hs.spd.SpdExponential;
import ch.alpine.sophus.itp.InverseDistanceWeighting;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;

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
public record MetricBiinvariant(TensorScalarFunction tensorScalarFunction) implements Biinvariant, Serializable {
  /** Careful: not suitable for {@link SpdExponential}, and {@link GrExponential}
   * because these implementations drop coefficients of the log in the vectorLog
   * implementation. that means the scalar product on the subspace would have to be
   * adapted.
   * 
   * scalar product has diagonal of all ones, i.e. [1, 1, ..., 1] */
  public static final Biinvariant EUCLIDEAN = new MetricBiinvariant(Vector2Norm::of);
  /** for {@link SpdExponential}, and {@link GrExponential} */
  public static final Biinvariant VECTORIZE0 = new MetricBiinvariant(LowerVectorize0_2Norm.INSTANCE::norm);

  /** @param tensorScalarFunction norm */
  public MetricBiinvariant {
    Objects.requireNonNull(tensorScalarFunction);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(Manifold manifold, Tensor sequence) {
    Objects.requireNonNull(manifold);
    Objects.requireNonNull(sequence);
    return point -> Tensor.of(new HsDesign(manifold).stream(sequence, point) //
        .map(tensorScalarFunction));
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence) {
    Genesis genesis = new MetricCoordinate(new InverseDistanceWeighting(variogram, tensorScalarFunction));
    return HsGenesis.wrap(manifold, genesis, sequence);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator lagrainate(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(manifold);
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = new HsDesign(manifold).matrix(sequence, point);
      return LagrangeCoordinates.of( //
          levers, //
          NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
              .map(tensorScalarFunction) //
              .map(variogram))));
    };
  }
}
