// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.MatrixExp;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.mat.qr.QRMathematica;

/** In the literature, the Stiefel manifold is denoted either as
 * St(n, k), or V_k(R^n)
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
  private final Tensor p;
  private final TStMemberQ tStMemberQ;

  /** @param p orthogonal matrix with dimensions k x n
   * @throws Exception if p is not member of Stiefel manifold
   * @see StMemberQ
   * @see OrthogonalMatrixQ */
  public StExponential(Tensor p) {
    this.p = StMemberQ.INSTANCE.require(p);
    tStMemberQ = new TStMemberQ(p);
  }

  @Override
  public Tensor exp(Tensor v) {
    tStMemberQ.require(v);
    Tensor vt = Transpose.of(v);
    Tensor a = p.dot(vt);
    // influence residual maker
    Tensor design = Transpose.of(p);
    Tensor ka = vt.subtract(design.dot(a));
    QRDecomposition qrDecomposition = QRMathematica.wrap(QRDecomposition.of(ka));
    Tensor r = qrDecomposition.getR();
    Tensor block = ArrayFlatten.of(new Tensor[][] { // antisym.
        { a, Transpose.of(r.negate()) }, //
        { r, a.map(Scalar::zero) } });
    Tensor mex = MatrixExp.of(block);
    int k = p.length();
    TensorUnaryOperator truncate = row -> row.extract(0, k);
    Tensor fp = Tensor.of(mex.stream().limit(k).map(truncate));
    Tensor fq = Tensor.of(mex.stream().skip(k).map(truncate));
    return Transpose.of(design.dot(fp).add(qrDecomposition.getQ().dot(fq)));
  }

  @Override
  public Tensor log(Tensor q) {
    return null;
  }

  @Override
  public Tensor vectorLog(Tensor q) {
    return null;
  }
}
