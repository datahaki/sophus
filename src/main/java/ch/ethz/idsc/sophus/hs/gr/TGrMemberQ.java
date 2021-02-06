// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** tangent space at given point x */
public class TGrMemberQ implements MemberQ, Serializable {
  private final Tensor x;

  public TGrMemberQ(Tensor x) {
    this.x = Objects.requireNonNull(x);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return SymmetricMatrixQ.of(v, Tolerance.CHOP) //
        && Tolerance.CHOP.isClose(x.dot(v).add(v.dot(x)), v);
  }
}
