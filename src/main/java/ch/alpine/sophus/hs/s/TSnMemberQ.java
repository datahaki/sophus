// code by jph
package ch.alpine.sophus.hs.s;

import java.util.Objects;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.sca.Chop;

public class TSnMemberQ extends ZeroDefectArrayQ {
  private final Tensor p;

  public TSnMemberQ(Tensor p, Chop chop) {
    super(1, chop);
    this.p = Objects.requireNonNull(p);
  }

  public TSnMemberQ(Tensor p) {
    this(p, Chop._06);
  }

  @Override // from ZeroDefectArrayQ
  public Tensor defect(Tensor v) {
    return p.dot(v);
  }

  /** projection is idempotent
   * 
   * ( Id - p'.p ) . v
   * 
   * @param v vector
   * @return the projection of given v to the plane orthogonal to the base point x */
  public Tensor projection(Tensor v) {
    return v.subtract(p.multiply((Scalar) p.dot(v)));
  }
}
