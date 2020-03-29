// code by jph
package ch.ethz.idsc.sophus.crv.hermite;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.java.util.IntegerFunction;
import ch.ethz.idsc.sophus.flt.ga.GeodesicCenter;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/* package */ class Hermite3SubdivisionBuilder implements Serializable {
  private final LieGroup lieGroup;
  private final Exponential exponential;
  private final Tensor cgw;
  private final Scalar mgv;
  private final Scalar mvg;
  private final Scalar mvv;
  private final Scalar cgv;
  private final Scalar vpr;
  private final Tensor vpqr;

  public Hermite3SubdivisionBuilder(LieGroup lieGroup, Exponential exponential, //
      Tensor cgw, //
      Scalar mgv, Scalar mvg, Scalar mvv, //
      Scalar cgv, Scalar vpr, Tensor vpqr) {
    this.lieGroup = lieGroup;
    this.exponential = exponential;
    this.cgw = cgw;
    this.mgv = mgv;
    this.mvg = mvg;
    this.mvv = mvv;
    this.cgv = cgv;
    this.vpr = vpr;
    this.vpqr = vpqr;
  }

  /** @param biinvariantMean
   * @return Hermite subdivision operator that uses the given biinvariant Mean
   * for computing weighted averages in the Lie group */
  public HermiteSubdivision create(BiinvariantMean biinvariantMean) {
    Objects.requireNonNull(biinvariantMean);
    return get(pqr -> biinvariantMean.mean(pqr, cgw));
  }

  /** @return Hermite subdivision operator that uses the geodesic center
   * for computing weighted averages in the Lie group */
  public HermiteSubdivision create() {
    IntegerFunction<Tensor> integerTensorFunction = i -> cgw;
    return get(GeodesicCenter.of(new HsGeodesic(LieExponential.of(lieGroup, exponential)), integerTensorFunction));
  }

  private HermiteSubdivision get(TensorUnaryOperator tripleCenter) {
    return new Hermite3Subdivision(lieGroup, exponential, //
        tripleCenter, //
        mgv, mvg, mvv, //
        cgv, vpr, vpqr);
  }
}
