// code by jph
package ch.ethz.idsc.sophus.crv.hermite;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupGeodesic;
import ch.ethz.idsc.sophus.math.TensorIteration;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/**  */
@Deprecated
public class Hermite1Filter implements HermiteFilter {
  private static final Scalar _1_4 = RationalScalar.of(1, 4);
  // ---
  private final LieGroup lieGroup;
  private final LieExponential lieExponential;
  private final LieGroupGeodesic lieGroupGeodesic;

  /** @param lieGroup
   * @param lieExponential
   * @throws Exception if either parameters is null */
  public Hermite1Filter(LieGroup lieGroup, LieExponential lieExponential) {
    this.lieGroup = lieGroup;
    this.lieExponential = lieExponential;
    lieGroupGeodesic = new LieGroupGeodesic(lieGroup, lieExponential);
  }

  @Override // from HermiteFilter
  public TensorIteration string(Scalar delta, Tensor control) {
    return new Control(delta, control).new StringIteration();
  }

  private class Control {
    private Tensor control;
    private Scalar rgk;
    private Scalar rvk;

    private Control(Scalar delta, Tensor control) {
      this.control = control;
      rgk = RealScalar.of(8).divide(delta);
      rvk = RationalScalar.of(3, 2).divide(delta);
    }

    /** @param p == {pg, pv}
     * @param q == {qg, qv}
     * @return r == {rg, rv} */
    private Tensor midpoint(Tensor p, Tensor q) {
      Tensor pg = p.get(0);
      Tensor pv = p.get(1);
      Tensor qg = q.get(0);
      Tensor qv = q.get(1);
      Tensor rg1 = lieGroupGeodesic.midpoint(pg, qg);
      Tensor rg2 = lieExponential.exp(pv.subtract(qv).divide(rgk));
      Tensor rg = lieGroup.element(rg1).combine(rg2);
      Tensor log = lieExponential.log(lieGroup.element(pg).inverse().combine(qg));
      Tensor rv1 = log.multiply(rvk);
      Tensor rv2 = qv.add(pv).multiply(_1_4);
      Tensor rv = rv1.subtract(rv2);
      return Tensors.of(rg, rv);
    }

    private class StringIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        int length = control.length();
        Tensor string = Tensors.reserve(length - 1);
        Tensor p = control.get(0);
        for (int index = 1; index < length; ++index) {
          Tensor q = control.get(index);
          string.append(midpoint(p, q));
          p = q;
        }
        return control = string;
      }
    }
  }
}
