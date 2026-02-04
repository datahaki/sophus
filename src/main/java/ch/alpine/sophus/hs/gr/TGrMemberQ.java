// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.lie.MatrixBracket;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.chq.ConstraintSquareMatrixQ;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.sca.Chop;

/** tangent space at given point x
 * 
 * The dimensionality of TGr(n, k) is k * (n - k). */
public class TGrMemberQ extends ConstraintSquareMatrixQ {
  private final Tensor p;

  /** @param p in Gr(n, k) */
  public TGrMemberQ(Tensor p) {
    super(Chop._10);
    this.p = p;
  }

  @Override
  public Tensor constraint(Tensor v) {
    return Join.of( //
        p.dot(v).add(v.dot(p)).subtract(v), //
        HermitianMatrixQ.INSTANCE.constraint(v));
  }

  /** Reference:
   * "Geometric mean and geodesic regression on Grassmannians"
   * by E. Batzies, K. Hueper, L. Machado, F. Silva Leite, 2015
   * 
   * @param p
   * @param v
   * @return [p, [p, v]] == v */
  public static Tensor identity(Tensor p, Tensor v) {
    return MatrixBracket.of(p, MatrixBracket.of(p, v)).subtract(v);
  }

  /** NOTE: instead use projection function from homogenous span
   * 
   * idempotent projection
   * equivalent to least squares projection
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
   * @param p
   * @param v square matrix
   * @return square matrix that is a tangent vector in T_p(Gr) */
  @PackageTestAccess
  Tensor projection(Tensor v) {
    v = Symmetrize.of(v);
    return MatrixBracket.of(p, MatrixBracket.of(p, v));
  }
}
