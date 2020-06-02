// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dot;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** Reference:
 * Geomstats: A Python Package for Riemannian Geometry in Machine Learning
 * by Nina Miolane, Alice Le Brigant, Johan Mathe, Benjamin Hou et al., 2020 */
public class GrExponential implements Exponential, TangentSpace, Serializable {
  private final Tensor x;
  private final Tensor id;

  /** @param x rank k projector of Gr(n, k) */
  public GrExponential(Tensor x) {
    this.x = x;
    id = IdentityMatrix.of(x.length());
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    AntisymmetricMatrixQ.require(v);
    return Dot.of(MatrixExp.of(v), x, MatrixExp.of(v.negate())); // TODO replace with inverse?
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return MatrixLog.of(y.add(y).subtract(id).dot(x.add(x).subtract(id))).multiply(RationalScalar.HALF);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return Flatten.of(log(y));
  }
}
