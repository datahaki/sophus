// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** Merrien interpolatory Hermite subdivision scheme of order two
 * implementation for R^n */
public class Hermite2Subdivision implements HermiteSubdivision, Serializable {
  private final HomogeneousSpace homogeneousSpace;
  private final HsTransport hsTransport;
  private final Scalar lgg;
  private final Scalar lgv;
  private final Scalar hgv;
  private final Scalar hgg;
  private final Scalar hvg;
  private final Tensor vpq;

  /** @param homogeneousSpace
   * @param lgg
   * @param lgv
   * @param hgv
   * @param hvg
   * @param vpq
   * @throws Exception if either parameters is null */
  public Hermite2Subdivision( //
      HomogeneousSpace homogeneousSpace, //
      Scalar lgg, Scalar lgv, Scalar hgv, Scalar hvg, Tensor vpq) {
    this.homogeneousSpace = homogeneousSpace;
    hsTransport = homogeneousSpace.hsTransport();
    this.lgg = lgg;
    hgg = RealScalar.ONE.subtract(this.lgg);
    this.lgv = Objects.requireNonNull(lgv);
    this.hgv = Objects.requireNonNull(hgv);
    this.hvg = hvg.add(hvg);
    this.vpq = VectorQ.requireLength(vpq.add(vpq), 2);
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
    private Scalar rgp;
    private Scalar rgq;
    private Scalar rvk;

    private Control(Scalar delta, Tensor control) {
      this.control = control;
      rgp = delta.multiply(lgv);
      rgq = delta.multiply(hgv).negate();
      rvk = hvg.divide(delta);
    }

    private void refine(Tensor curve, Tensor p, Tensor q) {
      Tensor pg = p.get(0);
      Tensor pv = p.get(1);
      Tensor qg = q.get(0);
      Tensor qv = q.get(1);
      // ---
      ScalarTensorFunction scalarTensorFunction = homogeneousSpace.curve(pg, qg);
      {
        Tensor rg1 = scalarTensorFunction.apply(lgg); // initial guess
        Tensor rg1v1 = hsTransport.shift(pg, rg1).apply(pv.multiply(rgp));
        Tensor rg1v2 = hsTransport.shift(qg, rg1).apply(qv.multiply(rgq));
        Tensor rv1df = rg1v1.subtract(rg1v2);
        Tensor rg = homogeneousSpace.exponential(rg1).exp(rv1df);
        // ---
        Exponential exponential = homogeneousSpace.exponential(rg);
        Tensor lrp = exponential.log(pg); // p - r
        Tensor lrq = exponential.log(qg); // q - r
        Tensor rv1 = lrq.subtract(lrp).multiply(rvk);
        // ---
        Tensor rgv1 = hsTransport.shift(pg, rg).apply(pv);
        Tensor rgv2 = hsTransport.shift(qg, rg).apply(qv);
        Tensor rv2 = vpq.dot(Tensors.of(rgv1, rgv2));
        Tensor rv = rv1.add(rv2);
        curve.append(Tensors.of(rg, rv));
      }
      {
        Tensor rg1 = scalarTensorFunction.apply(hgg); // initial guess
        Tensor rg1v1 = hsTransport.shift(pg, rg1).apply(pv.multiply(rgq));
        Tensor rg1v2 = hsTransport.shift(qg, rg1).apply(qv.multiply(rgp));
        Tensor rv1df = rg1v1.subtract(rg1v2);
        Tensor rg = homogeneousSpace.exponential(rg1).exp(rv1df);
        // ---
        Exponential exponential = homogeneousSpace.exponential(rg);
        Tensor lrp = exponential.log(pg); // p - r
        Tensor lrq = exponential.log(qg); // q - r
        Tensor rv1 = lrq.subtract(lrp).multiply(rvk);
        Tensor rgv1 = hsTransport.shift(pg, rg).apply(pv);
        Tensor rgv2 = hsTransport.shift(qg, rg).apply(qv);
        Tensor rv2 = vpq.dot(Tensors.of(rgv2, rgv1));
        Tensor rv = rv1.add(rv2);
        curve.append(Tensors.of(rg, rv));
      }
    }

    private Tensor protected_string(Tensor tensor) {
      int length = tensor.length();
      Tensor curve = Tensors.reserve(2 * length); // allocation for cyclic case
      Iterator<Tensor> iterator = tensor.iterator();
      Tensor p = iterator.next();
      while (iterator.hasNext()) {
        Tensor q = iterator.next();
        refine(curve, p, q);
        p = q;
      }
      return curve;
    }

    private class StringIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        Tensor curve = protected_string(control);
        rgp = rgp.multiply(RationalScalar.HALF);
        rgq = rgq.multiply(RationalScalar.HALF);
        rvk = rvk.add(rvk);
        return control = curve;
      }
    }

    private class CyclicIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        Tensor curve = protected_string(control);
        refine(curve, Last.of(control), control.get(0));
        rgp = rgp.multiply(RationalScalar.HALF);
        rgq = rgq.multiply(RationalScalar.HALF);
        rvk = rvk.add(rvk);
        return control = curve;
      }
    }
  }
}
