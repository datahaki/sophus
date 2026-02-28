// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.lie.rot.RotationMatrix;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.tri.ArcTan;

public record Sl2Iwasawa(Scalar theta, Scalar t, Scalar s) {
  /** @param matrix in SL(2) i.e. with det close to 1
   * @return */
  public static Sl2Iwasawa from(Tensor matrix) {
    Chop._02.requireClose(Det.of(matrix), RealScalar.ONE);
    Scalar a = matrix.Get(0, 0);
    Scalar b = matrix.Get(0, 1);
    Scalar c = matrix.Get(1, 0);
    Scalar d = matrix.Get(1, 1);
    Scalar den = a.multiply(a).add(c.multiply(c));
    Scalar t = Log.FUNCTION.apply(den);
    Scalar theta = ArcTan.of(a, c);
    Scalar s = a.multiply(b).add(c.multiply(d)).divide(den);
    return new Sl2Iwasawa(theta, t, s);
  }

  public static Sl2Iwasawa of(Number theta, Number t, Number s) {
    return new Sl2Iwasawa(RealScalar.of(theta), RealScalar.of(t), RealScalar.of(s));
  }

  public Tensor K() {
    return RotationMatrix.of(theta);
  }

  public Tensor A() {
    Scalar t_2 = t.multiply(Rational.HALF);
    return DiagonalMatrix.full(Tensors.of(t_2, t_2.negate()).maps(Exp.FUNCTION));
  }

  public Tensor N() {
    Tensor id = IdentityMatrix.of(2);
    id.set(s, 0, 1);
    return id;
  }

  public Tensor matrix() {
    return Dot.of(K(), A(), N());
  }
}
