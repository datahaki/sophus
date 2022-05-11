// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.flt.ga.GeodesicCenter;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/* package */ class Hermite3SubdivisionBuilder implements Serializable {
  private final HomogeneousSpace homogeneousSpace;
  private final Tensor cgw;
  private final Scalar mgv;
  private final Scalar mvg;
  private final Scalar mvv;
  private final Scalar cgv;
  private final Scalar vpr;
  private final Tensor vpqr;

  public Hermite3SubdivisionBuilder( //
      HomogeneousSpace homogeneousSpace, //
      Tensor cgw, //
      Scalar mgv, Scalar mvg, Scalar mvv, //
      Scalar cgv, Scalar vpr, Tensor vpqr) {
    this.homogeneousSpace = homogeneousSpace;
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
    @SuppressWarnings("unchecked")
    Function<Integer, Tensor> function = (Function<Integer, Tensor> & Serializable) i -> cgw;
    return get(GeodesicCenter.of(homogeneousSpace, function));
  }

  private HermiteSubdivision get(TensorUnaryOperator tripleCenter) {
    return new Hermite3Subdivision(homogeneousSpace, //
        tripleCenter, //
        mgv, mvg, mvv, //
        cgv, vpr, vpqr);
  }
}
