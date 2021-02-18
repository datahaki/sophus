// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

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

  /** Cafeful: projection is not idempotent!!!
   * duplicate application maps to zero
   * 
   * Reference: geomstats - grassmannian.py
   * 
   * @param v
   * @return */
  public Tensor forceProject(Tensor v) {
    // LONGTERM find projection that is idempotent
    return MatrixBracket.of(p, TensorWedge.of(v));
  }
}
