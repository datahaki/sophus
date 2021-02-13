// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020 */
public class GrExponential implements Exponential, Serializable {
  private final Tensor x;
  private final TGrMemberQ tGrMemberQ;
  private final Tensor id;
  private final Tensor x2_id;

  /** @param x rank k projector of Gr(n, k)
   * @throws Exception if x is not an element in the Grassmann manifold */
  public GrExponential(Tensor x) {
    this.x = GrMemberQ.INSTANCE.require(x);
    tGrMemberQ = new TGrMemberQ(x);
    id = IdentityMatrix.of(x.length());
    x2_id = p2_id(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return BasisTransform.ofMatrix(x, MatrixExp.of(MatrixBracket.of(x, tGrMemberQ.require(v))));
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    GrMemberQ.INSTANCE.require(y);
    Tensor v = MatrixBracket.of(MatrixLog.of(p2_id(y).dot(x2_id)).multiply(RationalScalar.HALF), x);
    tGrMemberQ.require(v);
    return v; // tGrMemberQ.project(v);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return Flatten.of(log(y));
  }

  /** @param p
   * @return p * 2 - id */
  private Tensor p2_id(Tensor p) {
    return p.add(p).subtract(id);
  }
}
