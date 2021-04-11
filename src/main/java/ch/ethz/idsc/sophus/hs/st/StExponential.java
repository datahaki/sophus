// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.MatrixDotTranspose;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.mat.QRDecomposition;

/** In the literature, the Stiefel manifold is denoted either as
 * St(n,p), or V_k(R^n)
 * 
 * Reference:
 * "A matrix-algebraic algorithm for the Riemannian logarithm on the Stiefel manifold
 * under the canonical metric"
 * by Ralf Zimmermann, 2017
 * https://arxiv.org/pdf/1604.05054.pdf
 * 
 * "Eichfeldtheorie"
 * by Helga Baum, 2005
 * 
 * Reference: geomstats */
public class StExponential implements Exponential, Serializable {
  private final Tensor x;
  private final TStMemberQ tStMemberQ;

  /** @param x column-orthogonal rectangular matrix with dimensions n x p */
  public StExponential(Tensor x) {
    this.x = StMemberQ.INSTANCE.require(x);
    tStMemberQ = new TStMemberQ(x);
  }

  @Override
  public Tensor exp(Tensor v) {
    tStMemberQ.require(v);
    Tensor a = MatrixDotTranspose.of(x, v);
    Tensor k = v.subtract(a.dot(x)); // TODO check sign
    Tensor kt = Transpose.of(k);
    QRDecomposition qrDecomposition = QRDecomposition.of(kt);
    Tensor ar = Join.of(a, Transpose.of(qrDecomposition.getR().negate())); // TODO check dim
    Tensor zeros = v.map(Scalar::zero);
    Tensor rz = Join.of(qrDecomposition.getR(), zeros); // TODO check dim
    Tensor block = Join.of(ar, rz);
    Tensor mn_e = MatrixExp.of(block);
    return x.dot(mn_e).add(qrDecomposition.getQ().dot(mn_e));
  }

  @Override
  public Tensor log(Tensor y) {
    return null;
  }

  @Override
  public Tensor vectorLog(Tensor y) {
    return null;
  }
}
