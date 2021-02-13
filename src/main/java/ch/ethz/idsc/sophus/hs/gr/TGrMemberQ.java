// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

/** tangent space at given point x
 * 
 * The dimensionality of TGr(n, k) is k * (n - k). */
public class TGrMemberQ implements MemberQ, Serializable {
  private static final Chop CHOP = Chop._06;
  private final Tensor x;

  /** @param x in Gr(n, k) */
  public TGrMemberQ(Tensor x) {
    this.x = Objects.requireNonNull(x);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return SymmetricMatrixQ.of(v, CHOP) //
        && CHOP.isClose(x.dot(v).add(v.dot(x)), v);
  }
}
