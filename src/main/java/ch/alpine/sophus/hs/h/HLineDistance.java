// code by jph
package ch.alpine.sophus.hs.h;

import java.io.Serializable;

import ch.alpine.sophus.math.api.LineDistance;
import ch.alpine.sophus.math.api.TensorDistance;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.NullSpace;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.tri.ArcSinh;

/** distance between line spanned by p and q on S^n and point r on S^n */
public enum HLineDistance implements LineDistance {
  INSTANCE;

  @Override // from LineDistance
  public TensorDistance distanceToLine(Tensor p, Tensor q) {
    return new TensorDistanceImpl(p, q);
  }

  private static class TensorDistanceImpl implements TensorDistance, Serializable {
    private final Tensor m;

    public TensorDistanceImpl(Tensor p, Tensor q) {
      HWeierstrassCoordinate pw = new HWeierstrassCoordinate(p);
      HWeierstrassCoordinate qw = new HWeierstrassCoordinate(q);
      Tensor A = Tensors.of(pw.toPoint(), qw.toPoint());
      Tensor d = Array.same(RealScalar.ONE, p.length()).append(RealScalar.ONE.negate());
      Tensor matrix = Tensor.of(A.stream().map(row -> Times.of(row, d)));
      // Tensor diag = DiagonalMatrix.full(d);
      // Tensor matrix = A.dot(diag);
      // Tolerance.CHOP.requireClose(matriy, matrix);
      Tensor nullspace = NullSpace.of(matrix);
      Integers.requireEquals(nullspace.length(), 1);
      // TODO implementation restricted to H^2 ORTHOGINALIZE!!!
      Scalar norm = LBilinearForm.INSTANCE.norm(nullspace.get(0));
      Tensor n = nullspace.get(0).divide(norm);
      Scalar normCheck = LBilinearForm.INSTANCE.norm(n);
      Tolerance.CHOP.requireClose(normCheck, RealScalar.ONE);
      m = Tensors.of(n);
    }

    @Override // from TensorNorm
    public Scalar distance(Tensor r) {
      HWeierstrassCoordinate rw = new HWeierstrassCoordinate(r);
      Tensor rx = rw.toPoint();
      Tensor vec = Tensor.of(m.stream().map(n -> LBilinearForm.INSTANCE.formEval(n, rx)));
      return ArcSinh.FUNCTION.apply(Clips.unit().apply(Vector2Norm.of(vec)));
    }
  }
}
