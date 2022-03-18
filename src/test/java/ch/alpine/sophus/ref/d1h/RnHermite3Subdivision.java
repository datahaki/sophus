// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.ref.d1.BSpline3CurveSubdivision;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.ex.MatrixPower;
import ch.alpine.tensor.sca.Chop;

/** Merrien interpolatory Hermite subdivision scheme of order two
 * reproduces polynomials up to degree 3
 * 
 * implementation for R^n
 * 
 * Reference 1:
 * "Dual Hermite subdivision schemes of de Rham-type", 2014
 * by Conti, Merrien, Romani
 * 
 * Reference 2:
 * "A note on spectral properties of Hermite subdivision operators"
 * Example 14, p. 13
 * by Moosmueller, 2018
 * 
 * @see BSpline3CurveSubdivision */
/* package */ class RnHermite3Subdivision implements HermiteSubdivision {
  private static final Tensor DIAG = DiagonalMatrix.of(RealScalar.ONE, RationalScalar.HALF);
  // ---
  private final Tensor AMP;
  private final Tensor AMQ;
  // ---
  final Tensor ARP;
  final Tensor ARQ;
  final Tensor ARR;

  /** "Construction of Hermite subdivision schemes reproducing polynomials", 2017
   * Example 3.7, eq. 28, p. 572
   * by Byeongseon Jeong, Jungho Yoon
   * 
   * @param theta
   * @param omega */
  public RnHermite3Subdivision(Tensor AMP, Tensor AMQ, Tensor ARP, Tensor ARQ, Tensor ARR) {
    this.AMP = AMP;
    this.AMQ = AMQ;
    this.ARP = ARP;
    this.ARQ = ARQ;
    this.ARR = ARR;
  }

  @Override // from HermiteSubdivision
  public TensorIteration string(Scalar delta, Tensor control) {
    Chop.NONE.requireClose(delta, RealScalar.ONE);
    return new Control(control).new StringIteration();
  }

  @Override // from HermiteSubdivision
  public TensorIteration cyclic(Scalar delta, Tensor control) {
    Chop.NONE.requireClose(delta, RealScalar.ONE);
    return new Control(control).new CyclicIteration();
  }

  private class Control {
    private Tensor control;
    private int k = 0;

    private Control(Tensor control) {
      this.control = control;
    }

    private class StringIteration implements TensorIteration {
      @Override // from TensorIteration
      public Tensor iterate() {
        int length = control.length();
        Tensor string = Tensors.reserve(2 * length - 1);
        Tensor Dk = MatrixPower.of(DIAG, k);
        Tensor Dnk1 = MatrixPower.of(DIAG, -(k + 1));
        Tensor amp = Dot.of(Dnk1, AMP, Dk);
        Tensor amq = Dot.of(Dnk1, AMQ, Dk);
        Tensor arp = Dot.of(Dnk1, ARP, Dk);
        Tensor arq = Dot.of(Dnk1, ARQ, Dk);
        Tensor arr = Dot.of(Dnk1, ARR, Dk);
        Tensor p = control.get(0);
        Tensor q = control.get(1);
        string.append(p);
        string.append(amp.dot(p).add(amq.dot(q)));
        for (int index = 1; index < length - 1; ++index) {
          Tensor r = control.get(index + 1);
          string.append(arp.dot(p).add(arq.dot(q)).add(arr.dot(r)));
          p = q;
          q = r;
          string.append(amp.dot(p).add(amq.dot(q)));
        }
        string.append(Last.of(control));
        ++k;
        return control = string;
      }
    }

    private class CyclicIteration implements TensorIteration {
      @Override // from TensorIteration
      public Tensor iterate() {
        int length = control.length();
        Tensor string = Tensors.reserve(2 * length);
        Tensor Dk = MatrixPower.of(DIAG, k);
        Tensor Dnk1 = MatrixPower.of(DIAG, -(k + 1));
        Tensor amp = Dot.of(Dnk1, AMP, Dk);
        Tensor amq = Dot.of(Dnk1, AMQ, Dk);
        Tensor arp = Dot.of(Dnk1, ARP, Dk);
        Tensor arq = Dot.of(Dnk1, ARQ, Dk);
        Tensor arr = Dot.of(Dnk1, ARR, Dk);
        Tensor p = Last.of(control);
        Tensor q = control.get(0);
        for (int index = 0; index < length; ++index) {
          Tensor r = control.get((index + 1) % length);
          string.append(arp.dot(p).add(arq.dot(q)).add(arr.dot(r)));
          p = q;
          q = r;
          string.append(amp.dot(p).add(amq.dot(q)));
        }
        ++k;
        return control = string;
      }
    }
  }
}
