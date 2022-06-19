// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.lie.MatrixBracket;
import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

/** tangent space at given point x
 * 
 * The dimensionality of TGr(n, k) is k * (n - k). */
public class TGrMemberQ implements MemberQ, Serializable {
  private static final Chop CHOP = Chop._06;
  // ---
  private final Tensor p;

  /** @param p in Gr(n, k) */
  public TGrMemberQ(Tensor p) {
    this.p = Objects.requireNonNull(p);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return SymmetricMatrixQ.of(v, CHOP) //
        && CHOP.isClose(p.dot(v).add(v.dot(p)), v);
  }

  /** idempotent projection
   * 
   * Reference: geomstats - grassmannian.py
   * 
   * Remark:
   * an earlier version, geomstats implemented the following
   * non-idempotent(!) projection
   * <pre>
   * MatrixBracket.of(p, TensorWedge.of(v));
   * </pre>
   * 
   * @param v square matrix
   * @return square matrix that is a tangent vector in T_p(Gr) */
  public Tensor projection(Tensor v) {
    v = Symmetrize.of(v);
    return MatrixBracket.of(p, MatrixBracket.of(p, v));
  }
}
