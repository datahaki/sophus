// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.ext.Integers;

public class Hermite1Subdivision implements HermiteSubdivision, Serializable {
  private final HsManifold hsManifold;
  private final HsTransport hsTransport;
  private final Scalar lgv;
  private final Scalar lvg;
  private final Scalar lvv;

  /** @param hsManifold
   * @param hsTransport
   * @param lgv
   * @param lvg
   * @param lvv
   * @return
   * @throws Exception if either parameters is null */
  public Hermite1Subdivision( //
      HsManifold hsManifold, Scalar lgv, Scalar lvg, Scalar lvv) {
    this.hsManifold = hsManifold;
    hsTransport = hsManifold.hsTransport();
    this.lgv = Objects.requireNonNull(lgv);
    this.lvg = lvg.add(lvg);
    this.lvv = lvv.add(lvv);
  }

  @Override // from HermiteSubdivision
  public TensorIteration string(Scalar delta, Tensor control) {
    return new Control(delta, control).new StringIteration();
  }

  @Override // from HermiteSubdivision
  public TensorIteration cyclic(Scalar delta, Tensor control) {
    return new Control(delta, control).new CyclicIteration();
  }

  private class Control {
    private Tensor control;
    private Scalar rgk;
    private Scalar rvk;

    private Control(Scalar delta, Tensor control) {
      this.control = control;
      rgk = delta.multiply(lgv).negate();
      rvk = lvg.divide(delta);
    }

    /** @param p == {pg, pv}
     * @param q == {qg, qv}
     * @return r == {rg, rv} */
    private Tensor midpoint(Tensor p, Tensor q) {
      Tensor pg = p.get(0);
      Tensor pv = p.get(1);
      Tensor qg = q.get(0);
      Tensor qv = q.get(1);
      final Tensor rg;
      {
        Tensor rg1 = hsManifold.midpoint(pg, qg);
        Tensor rpv = hsTransport.shift(pg, rg1).apply(pv); // at rg1
        Tensor rqv = hsTransport.shift(qg, rg1).apply(qv);
        rg = hsManifold.exponential(rg1).exp(rpv.subtract(rqv).multiply(rgk));
      }
      final Tensor rv1;
      {
        Exponential exponential = hsManifold.exponential(rg);
        Tensor lrq = exponential.log(qg); // at rg pointing to q
        Tensor lrp = exponential.log(pg); // at rg pointing to p
        rv1 = lrq.subtract(lrp).multiply(rvk); // at rg
      }
      final Tensor rv2;
      {
        Tensor rpv = hsTransport.shift(pg, rg).apply(pv); // at rg
        Tensor rqv = hsTransport.shift(qg, rg).apply(qv); // at rg
        rv2 = rpv.add(rqv).multiply(lvv); // at rg
      }
      Tensor rv = rv1.add(rv2); // average
      return Tensors.of(rg, rv);
    }

    private class StringIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        int length = control.length();
        List<Tensor> list = new ArrayList<>(2 * length - 1);
        Tensor p = control.get(0);
        for (int index = 1; index < length; ++index) {
          list.add(p);
          Tensor q = control.get(index);
          list.add(midpoint(p, q));
          p = q;
        }
        list.add(p);
        rgk = rgk.multiply(RationalScalar.HALF);
        rvk = rvk.add(rvk);
        Integers.requireEquals(list.size(), 2 * length - 1);
        return control = Unprotect.using(list);
      }
    }

    private class CyclicIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        int length = control.length();
        List<Tensor> list = new ArrayList<>(2 * length);
        Tensor p = control.get(0);
        for (int index = 1; index <= length; ++index) {
          list.add(p);
          Tensor q = control.get(index % length);
          list.add(midpoint(p, q));
          p = q;
        }
        rgk = rgk.multiply(RationalScalar.HALF);
        rvk = rvk.add(rvk);
        Integers.requireEquals(list.size(), 2 * length);
        return control = Unprotect.using(list);
      }
    }
  }
}
