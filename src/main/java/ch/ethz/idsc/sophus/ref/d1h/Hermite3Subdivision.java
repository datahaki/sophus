// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.Nocopy;
import ch.ethz.idsc.sophus.math.TensorIteration;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

public class Hermite3Subdivision implements HermiteSubdivision, Serializable {
  /** midpoint group element contribution from group elements
   * factor in position (1, 1) of matrices A(-1) A(1)
   * with same sign and equal to 1/2 */
  // ---
  private final HsExponential hsExponential;
  private final HsTransport hsTransport;
  private final HsGeodesic hsGeodesic;
  private final TensorUnaryOperator tripleCenter;
  private final Scalar mgv;
  private final Scalar mvv;
  private final Scalar mvg;
  private final Scalar cgv;
  private final Scalar cvg;
  /** for instance {-1/16, 3/4, -1/16} */
  private final Tensor cvw;

  /** @param lieGroup
   * @param exponential
   * @param tripleCenter
   * @param mgv
   * @param mvg
   * @param mvv
   * @param cgv
   * @param vpr
   * @param vpqr
   * @throws Exception if either parameters is null */
  public Hermite3Subdivision( //
      HsExponential hsExponential, HsTransport hsTransport, //
      TensorUnaryOperator tripleCenter, //
      Scalar mgv, Scalar mvg, Scalar mvv, //
      Scalar cgv, Scalar vpr, Tensor vpqr) {
    this.hsExponential = hsExponential;
    this.hsTransport = hsTransport;
    hsGeodesic = new HsGeodesic(hsExponential);
    this.tripleCenter = Objects.requireNonNull(tripleCenter);
    this.mgv = Objects.requireNonNull(mgv);
    this.mvg = mvg.add(mvg);
    this.mvv = mvv.add(mvv);
    // ---
    this.cgv = cgv;
    cvg = vpr.add(vpr);
    cvw = VectorQ.requireLength(vpqr.add(vpqr), 3);
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
    // ---
    private Scalar cgk;
    private Scalar cvk;

    private Control(Scalar delta, Tensor control) {
      this.control = control;
      rgk = delta.multiply(mgv);
      rvk = mvg.divide(delta);
      // ---
      cgk = delta.multiply(cgv);
      cvk = cvg.divide(delta);
    }

    private Tensor center(Tensor p, Tensor q, Tensor r) {
      Tensor pg = p.get(0);
      Tensor pv = p.get(1);
      Tensor qg = q.get(0);
      Tensor qv = q.get(1);
      Tensor rg = r.get(0);
      Tensor rv = r.get(1);
      Tensor cg1 = tripleCenter.apply(Unprotect.byRef(pg, qg, rg)); // initial guess
      Tensor cv1v1 = hsTransport.shift(pg, cg1).apply(pv); // at cg1
      Tensor cv1v2 = hsTransport.shift(rg, cg1).apply(rv); // at cg1
      Tensor cv1df = cv1v2.subtract(cv1v1).multiply(cgk);
      Tensor cg = hsExponential.exponential(cg1).exp(cv1df);
      // ---
      Exponential exponential = hsExponential.exponential(cg);
      Tensor clpg = exponential.log(pg); // p - c
      Tensor clrg = exponential.log(rg); // r - c
      Tensor cv1 = clrg.subtract(clpg).multiply(cvk); // r - p
      Tensor cpv = hsTransport.shift(pg, cg).apply(pv);
      Tensor cqv = hsTransport.shift(qg, cg).apply(qv);
      Tensor crv = hsTransport.shift(rg, cg).apply(rv);
      Tensor cv2 = cvw.dot(Unprotect.byRef(cpv, cqv, crv));
      Tensor cv = cv1.add(cv2);
      return Tensors.of(cg, cv);
    }

    /** @param p == {pg, pv}
     * @param q == {qg, qv}
     * @return r == {rg, rv} */
    private Tensor midpoint(Tensor p, Tensor q) {
      Tensor pg = p.get(0);
      Tensor pv = p.get(1);
      Tensor qg = q.get(0);
      Tensor qv = q.get(1);
      Tensor rg1 = hsGeodesic.midpoint(pg, qg); // initial guess
      Tensor rg1v1 = hsTransport.shift(pg, rg1).apply(pv);
      Tensor rg1v2 = hsTransport.shift(qg, rg1).apply(qv);
      Tensor rg1df = rg1v2.subtract(rg1v1).multiply(rgk);
      Tensor rg = hsExponential.exponential(rg1).exp(rg1df);
      // ---
      Exponential exponential = hsExponential.exponential(rg);
      Tensor rlpg = exponential.log(pg); // p - r
      Tensor rlqg = exponential.log(qg); // q - r
      Tensor rv1 = rlqg.subtract(rlpg).multiply(rvk); // q - p
      // ---
      Tensor rgv1 = hsTransport.shift(pg, rg).apply(pv);
      Tensor rgv2 = hsTransport.shift(qg, rg).apply(qv);
      Tensor rv2 = rgv2.add(rgv1).multiply(mvv);
      Tensor rv = rv1.add(rv2);
      return Tensors.of(rg, rv);
    }

    private class StringIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        int length = control.length();
        Nocopy string = new Nocopy(2 * length - 1);
        Tensor p = control.get(0);
        string.append(p); // interpolation
        Tensor q = control.get(1);
        string.append(midpoint(p, q));
        for (int index = 2; index < length; ++index) {
          Tensor r = control.get(index);
          string.append(center(p, q, r));
          p = q;
          q = r;
          string.append(midpoint(p, q));
        }
        string.append(q);
        rgk = rgk.multiply(RationalScalar.HALF);
        rvk = rvk.add(rvk);
        cgk = cgk.multiply(RationalScalar.HALF);
        cvk = cvk.add(cvk);
        return control = string.tensor();
      }
    }

    private class CyclicIteration implements TensorIteration {
      @Override // from HermiteSubdivision
      public Tensor iterate() {
        int length = control.length();
        Nocopy string = new Nocopy(2 * length);
        Tensor p = Last.of(control);
        Tensor q = control.get(0);
        for (int index = 1; index <= length; ++index) {
          Tensor r = control.get(index % length);
          string.append(center(p, q, r));
          p = q;
          q = r;
          string.append(midpoint(p, q));
        }
        rgk = rgk.multiply(RationalScalar.HALF);
        rvk = rvk.add(rvk);
        cgk = cgk.multiply(RationalScalar.HALF);
        cvk = cvk.add(cvk);
        return control = string.tensor();
      }
    }
  }
}
