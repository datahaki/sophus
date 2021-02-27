// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.LagrangeCoordinates;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.hs.gr.GrExponential;
import ch.ethz.idsc.sophus.hs.hn.HnMetricBiinvariant;
import ch.ethz.idsc.sophus.hs.spd.SpdExponential;
import ch.ethz.idsc.sophus.itp.InverseDistanceWeighting;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.sophus.math.Vectorize0Norm2;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

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
 * @see HnMetricBiinvariant */
public class MetricBiinvariant implements Biinvariant, Serializable {
  /** Careful: not suitable for {@link SpdExponential}, and {@link GrExponential}
   * because these implementations drop coefficients of the log in the vectorLog
   * implementation. that means the scalar product on the subspace would have to be
   * adapted.
   * 
   * scalar product has diagonal of all ones, i.e. [1, 1, ..., 1] */
  public static final Biinvariant EUCLIDEAN = new MetricBiinvariant(Vector2Norm::of);
  /** for {@link SpdExponential}, and {@link GrExponential} */
  public static final Biinvariant VECTORIZE0 = new MetricBiinvariant(Vectorize0Norm2.INSTANCE);

  /** @param tensorScalarFunction norm
   * @return */
  public static Biinvariant of(TensorScalarFunction tensorScalarFunction) {
    return new MetricBiinvariant(Objects.requireNonNull(tensorScalarFunction));
  }

  /***************************************************/
  private final TensorScalarFunction tensorScalarFunction;

  private MetricBiinvariant(TensorScalarFunction tensorScalarFunction) {
    this.tensorScalarFunction = tensorScalarFunction;
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    Objects.requireNonNull(vectorLogManifold);
    Objects.requireNonNull(sequence);
    return point -> Tensor.of(new HsDesign(vectorLogManifold).stream(sequence, point) //
        .map(tensorScalarFunction));
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    Genesis genesis = MetricCoordinate.of(InverseDistanceWeighting.of(variogram, tensorScalarFunction));
    return HsGenesis.wrap(vectorLogManifold, genesis, sequence);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator lagrainate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(vectorLogManifold);
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = new HsDesign(vectorLogManifold).matrix(sequence, point);
      return LagrangeCoordinates.of( //
          levers, //
          NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
              .map(tensorScalarFunction) //
              .map(variogram))));
    };
  }

  @Override // from Object
  public String toString() {
    return getClass().getSimpleName();
  }
}
