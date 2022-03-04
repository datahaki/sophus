// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;

import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.lie.ad.MatrixBracket;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.LowerVectorize;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020
 * 
 * use with {@link MetricBiinvariant#VECTORIZE0} */
public class GrExponential implements Exponential, Serializable {
  private static final Scalar N1_4 = RationalScalar.of(-1, 4);
  // ---
  private final Tensor p;
  private final TGrMemberQ tGrMemberQ;
  /** negative identity matrix */
  private final Tensor p2_id;

  /** @param p rank k projector of Gr(n, k)
   * @throws Exception if p is not an element in the Grassmann manifold */
  public GrExponential(Tensor p) {
    this.p = GrMemberQ.INSTANCE.require(p);
    tGrMemberQ = new TGrMemberQ(p);
    p2_id = StaticHelper.bic(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return BasisTransform.ofMatrix(p, MatrixExp.of(MatrixBracket.of(p, tGrMemberQ.require(v))));
  }

  private Tensor mLog(Tensor q) {
    return MatrixLog.of(StaticHelper.bic(q).dot(p2_id));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    GrMemberQ.INSTANCE.require(q);
    return MatrixBracket.of(mLog(q).multiply(RationalScalar.HALF), p);
  }

  @Override // from Exponential
  public Tensor flip(Tensor q) {
    // matrix bracket is obsolete
    return BasisTransform.ofMatrix(p, MatrixExp.of(mLog(q).multiply(RationalScalar.HALF)));
  }

  @Override // from Exponential
  public Tensor midpoint(Tensor q) {
    // matrix bracket is obsolete
    return BasisTransform.ofMatrix(p, MatrixExp.of(mLog(q).multiply(N1_4)));
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    // TODO k * (n - k) coefficients are sufficient according to theory
    return LowerVectorize.of(log(q), 0); // n (n + 1) / 2
  }
}
