// code by jph
package ch.ethz.idsc.sophus.crv.hermite;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;

public enum HermiteSubdivisions {
  HERMITE1() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite1Subdivisions.of(lieGroup, exponential, LAMBDA, MU);
    }
  }, //
  H1STANDARD() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite1Subdivisions.standard(lieGroup, exponential);
    }
  }, //
  /***************************************************/
  HERMITE2() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite2Subdivisions.of(lieGroup, exponential, LAMBDA, MU);
    }
  }, //
  H2STANDARD() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite2Subdivisions.standard(lieGroup, exponential);
    }
  }, //
  H2MANIFOLD() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite2Subdivisions.manifold(lieGroup, exponential);
    }
  }, //
  /***************************************************/
  HERMITE3() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.of(lieGroup, exponential, THETA, OMEGA);
    }
  }, //
  H3STANDARD() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.of(lieGroup, exponential);
    }
  }, //
  H3A1() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.a1(lieGroup, exponential);
    }
  }, //
  H3A2() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.a2(lieGroup, exponential);
    }
  }, //
  /***************************************************/
  HERMITE3_BM() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.of(lieGroup, exponential, biinvariantMean, THETA, OMEGA);
    }
  }, //
  H3STANDARD_BM() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.of(lieGroup, exponential, biinvariantMean);
    }
  }, //
  H3A1_BM() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.a1(lieGroup, exponential, biinvariantMean);
    }
  }, //
  H3A2_BM() {
    @Override
    public HermiteSubdivision supply(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
      return Hermite3Subdivisions.a2(lieGroup, exponential, biinvariantMean);
    }
  };

  public static Scalar LAMBDA = RationalScalar.of(-1, 8);
  public static Scalar MU = RationalScalar.of(-1, 2);
  public static Scalar THETA = RationalScalar.of(+1, 128);
  public static Scalar OMEGA = RationalScalar.of(-1, 16);

  /** @param lieGroup
   * @param exponential
   * @param biinvariantMean
   * @return
   * @throws Exception if either input parameter is null */
  public abstract HermiteSubdivision supply( //
      LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean);
}
