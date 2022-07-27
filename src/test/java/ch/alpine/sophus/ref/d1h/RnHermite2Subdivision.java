// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.sophus.math.api.TensorIteration;
import ch.alpine.sophus.ref.d1.BSpline2CurveSubdivision;
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
 * implementation for R^n
 *
 * @param ALP A(+0)
 * @param ALQ A(-2)
 * @param AHP A(+1)
 * @param AHQ A(-1)
 * 
 * @see BSpline2CurveSubdivision */
/* package */ record RnHermite2Subdivision(Tensor ALP, Tensor ALQ, Tensor AHP, Tensor AHQ) //
    implements HermiteSubdivision {

  private static final Tensor DIAG = DiagonalMatrix.of(RealScalar.ONE, RationalScalar.HALF);

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
        Tensor string = Tensors.reserve(2 * length - 2);
        Tensor Dk = MatrixPower.of(DIAG, k);
        Tensor Dnk1 = MatrixPower.of(DIAG, -(k + 1));
        Tensor alp = Dot.of(Dnk1, ALP, Dk);
        Tensor alq = Dot.of(Dnk1, ALQ, Dk);
        Tensor ahp = Dot.of(Dnk1, AHP, Dk);
        Tensor ahq = Dot.of(Dnk1, AHQ, Dk);
        Tensor p = control.get(0);
        for (int index = 1; index < length; ++index) {
          Tensor q = control.get(index);
          string.append(alp.dot(p).add(alq.dot(q)));
          string.append(ahp.dot(p).add(ahq.dot(q)));
          p = q;
        }
        ++k;
        return control = string;
      }
    }

    private class CyclicIteration implements TensorIteration {
      @Override // from TensorIteration
      public Tensor iterate() {
        int length = control.length();
        Tensor string = Tensors.reserve(2 * length - 2);
        Tensor Dk = MatrixPower.of(DIAG, k);
        Tensor Dnk1 = MatrixPower.of(DIAG, -(k + 1));
        Tensor alp = Dot.of(Dnk1, ALP, Dk);
        Tensor alq = Dot.of(Dnk1, ALQ, Dk);
        Tensor ahp = Dot.of(Dnk1, AHP, Dk);
        Tensor ahq = Dot.of(Dnk1, AHQ, Dk);
        Tensor p = control.get(0);
        for (int index = 1; index < length; ++index) {
          Tensor q = control.get(index);
          string.append(alp.dot(p).add(alq.dot(q)));
          string.append(ahp.dot(p).add(ahq.dot(q)));
          p = q;
        }
        {
          p = Last.of(control);
          Tensor q = control.get(0);
          string.append(alp.dot(p).add(alq.dot(q)));
          string.append(ahp.dot(p).add(ahq.dot(q)));
        }
        ++k;
        return control = string;
      }
    }
  }
}
