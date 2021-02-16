// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.Vectorize;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020
 * 
 * use with {@link MetricBiinvariant#VECTORIZE0} */
public class GrExponential implements Exponential, Serializable {
  private static final Scalar N1_4 = RationalScalar.of(-1, 4);
  private final Tensor p;
  private final TGrMemberQ tGrMemberQ;
  /** negative identity matrix */
  private final Tensor nid;
  private final Tensor p2_id;

  /** @param p rank k projector of Gr(n, k)
   * @throws Exception if p is not an element in the Grassmann manifold */
  public GrExponential(Tensor p) {
    this.p = GrMemberQ.INSTANCE.require(p);
    tGrMemberQ = new TGrMemberQ(p);
    nid = IdentityMatrix.of(p.length()).negate();
    p2_id = bic(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return BasisTransform.ofMatrix(p, MatrixExp.of(MatrixBracket.of(p, tGrMemberQ.require(v))));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    GrMemberQ.INSTANCE.require(q);
    return MatrixBracket.of(MatrixLog.of(bic(q).dot(p2_id)).multiply(RationalScalar.HALF), p);
  }

  @Override // from Exponential
  public Tensor flip(Tensor q) {
    // matrix bracket is obsolete
    Tensor v = MatrixExp.of(MatrixLog.of(bic(q).dot(p2_id)).multiply(RationalScalar.HALF));
    return BasisTransform.ofMatrix(p, v);
  }

  @Override // from Exponential
  public Tensor midpoint(Tensor q) {
    // matrix bracket is obsolete
    Tensor v = MatrixExp.of(MatrixLog.of(bic(q).dot(p2_id)).multiply(N1_4));
    return BasisTransform.ofMatrix(p, v);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    // TODO k * (n - k) coefficients are sufficient according to theory
    return Vectorize.of(log(q), 0);
  }

  /** @param q
   * @return q * 2 - id */
  private Tensor bic(Tensor q) {
    return q.add(nid).add(q);
  }
}
