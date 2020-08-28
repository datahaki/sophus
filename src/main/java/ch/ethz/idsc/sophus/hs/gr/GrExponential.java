// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020 */
public class GrExponential implements Exponential, TangentSpace, Serializable {
  private static final MemberQ MEMBER_Q = GrMemberQ.of(Tolerance.CHOP);
  // ---
  private final Tensor x;
  private final Tensor id;
  private final Tensor x2_i;

  /** @param x rank k projector of Gr(n, k) */
  public GrExponential(Tensor x) {
    this.x = x;
    id = IdentityMatrix.of(x.length());
    x2_i = p2_i(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    MEMBER_Q.requireTangent(x, v);
    Tensor exp = MatrixExp.of(MatrixBracket.of(x, v));
    return LinearSolve.of(exp, x).dot(exp);
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return MatrixBracket.of( //
        MatrixLog.of(p2_i(y).dot(x2_i)).multiply(RationalScalar.HALF), //
        x);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return Flatten.of(log(y));
  }

  /** @param p
   * @return
   * @throws Exception if p is not an element in the grassmannian manifold */
  private Tensor p2_i(Tensor p) {
    return MEMBER_Q.requirePoint(p).add(p).subtract(id);
  }
}
