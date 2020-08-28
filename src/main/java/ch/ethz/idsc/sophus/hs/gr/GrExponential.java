// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dot;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020 */
public class GrExponential implements Exponential, TangentSpace, Serializable {
  private final Tensor x;
  private final Tensor id;
  private final Tensor x2_i;

  /** @param x rank k projector of Gr(n, k) */
  public GrExponential(Tensor x) {
    this.x = GrassmannQ.require(x);
    id = IdentityMatrix.of(x.length());
    x2_i = x.add(x).subtract(id);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    StaticHelper.requireTangent(x, v, Chop._08);
    Tensor rot = MatrixBracket.of(x, v.negate());
    return Dot.of(MatrixExp.of(rot), x, MatrixExp.of(rot.negate())); // TODO replace with inverse?
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    Tensor y2_i = GrassmannQ.require(y).add(y).subtract(id);
    return MatrixBracket.of( //
        MatrixLog.of(y2_i.dot(x2_i)).multiply(RationalScalar.HALF), //
        x);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return Flatten.of(log(y));
  }
}
