// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.MatrixBracket;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020 */
public class GrExponential implements Exponential, Serializable {
  private final Tensor p;
  private final TGrMemberQ tGrMemberQ;
  /** negative identity matrix */
  private final Tensor p2_id;

  /** @param p rank k projector of Gr(n, k)
   * @throws Exception if p is not an element in the Grassmann manifold */
  public GrExponential(Tensor p) {
    this.p = GrManifold.INSTANCE.isPointQ().require(p);
    tGrMemberQ = new TGrMemberQ(p);
    p2_id = bic(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tGrMemberQ.require(v);
    return BasisTransform.ofMatrix(p, MatrixExp.of(MatrixBracket.of(p, v)));
  }

  /* package */ Tensor mLog(Tensor q) {
    return MatrixLog.of(bic(q).dot(p2_id));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    GrManifold.INSTANCE.isPointQ().require(q);
    return MatrixBracket.of(mLog(q).multiply(RationalScalar.HALF), p);
  }

  @Override
  public MemberQ isTangentQ() {
    return tGrMemberQ;
  }

  /** @param q
   * @return q * 2 - id */
  @PackageTestAccess
  static Tensor bic(Tensor p) {
    return IdentityMatrix.inplaceSub(p.add(p));
  }
}
