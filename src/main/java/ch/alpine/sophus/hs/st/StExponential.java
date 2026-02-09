// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;
import java.util.List;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.qr.GramSchmidt;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.mat.qr.QRMathematica;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.mat.sv.SingularValueDecomposition;
import ch.alpine.tensor.nrm.Matrix2Norm;
import ch.alpine.tensor.sca.Chop;

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
/* package */ class StExponential implements Exponential, Serializable {
  private final Tensor p;
  private final Tensor pt;
  private final MemberQ tStMemberQ;

  /** @param p orthogonal matrix with dimensions k x n
   * @throws Exception if p is not member of Stiefel manifold
   * @see StManifold
   * @see OrthogonalMatrixQ */
  public StExponential(Tensor p) {
    this.p = StManifold.INSTANCE.requireMember(p);
    pt = Transpose.of(p);
    tStMemberQ = new TStMemberQ(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tStMemberQ.requireMember(v);
    Tensor vt = Transpose.of(v);
    // ---
    Tensor a = TensorWedge.of(p.dot(vt)); // horizontal component, antisymmetric
    // influence residual maker
    Tensor ptpvt = pt.dot(a);
    Tensor ka = vt.subtract(ptpvt); // normal component
    QRDecomposition qrDecomposition = QRMathematica.wrap(QRDecomposition.of(ka));
    Tensor r = qrDecomposition.getR();
    Tensor Qt = qrDecomposition.getQConjugateTranspose();
    Tensor block = ArrayFlatten.of(new Tensor[][] { // AntisymmetricMatrixQ
        { a.negate(), Transpose.of(r) }, //
        { r.negate(), a.map(Scalar::zero) } });
    return MatrixExp.of(block).extract(0, p.length()).dot(Join.of(p, Qt));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) { // k x n
    // new InfluenceMatrixQ(Chop._08).requireMember(pt.dot(p));
    // InfluenceMatrix influenceMatrix = InfluenceMatrix.of(pt, p);
    // if k == n the Exponential coincides with SO_Exponential
    StManifold.INSTANCE.requireMember(q);
    Tensor qt = Transpose.of(q); // n x k
    int k = p.length();
    Integers.requireEquals(k, q.length());
    List<Integer> l00 = List.of(0, 0);
    List<Integer> l0k = List.of(0, k);
    List<Integer> lkk = List.of(k, k);
    // step 1
    final Tensor M = p.dot(qt); // k x k
    // step 2
    Tensor K = qt.subtract(pt.dot(M)); // n x k
    // IO.println(Pretty.of(K.map(Round._3)));
    // the MatrixRank of the matrix "K" is min(k, n-k)
    int n = qt.length();
    if (false)
      if (MatrixRank.of(K) != Math.min(k, n - k) && !Chop._10.allZero(K)) {
        IO.println(Pretty.of(K));
        System.err.println("NO NO NO");
      }
    QRDecomposition qrDecomposition = 2 * k <= n //
        ? GramSchmidt.of(K)
        : QRMathematica.wrap(QRDecomposition.of(K));
    final Tensor Qt = qrDecomposition.getQConjugateTranspose(); // k x n ( same dims as p,q )
    final Tensor N = qrDecomposition.getR(); // k x k
    // step 3
    Tensor P = Join.of(M, N); // 2k x k
    Tensor W = QRDecomposition.of(P).getQ(); // 2k x 2k | orthogonal completion
    // Procrustes preprocessing
    Tensor G = W.block(lkk, lkk); // k x k
    SingularValueDecomposition svd = SingularValueDecomposition.of(G);
    Tensor U = svd.getU(); // k x k
    Tensor R = svd.getV(); // k x k
    Tensor D = W.block(l0k, List.of(W.length(), k)); // 2k x k
    Tensor O = Dot.of(D, R, Transpose.of(U)); // 2k x k
    Tensor V = Join.of(1, P, O); // 2k x 2k orthogonal matrix
    // new OrthogonalMatrixQ(Chop._08).requireMember(V);
    // step 4: for loop
    int max = (k + 2) * qt.length();
    for (int i = 0; i < max; ++i) {
      // step 5
      Tensor L = MatrixLog.of(V).negate(); // 2k x 2k antisymmetric
      new AntisymmetricMatrixQ(Chop._08).requireMember(L);
      L = TensorWedge.of(L); // just for numerical correction
      Tensor C = L.block(lkk, lkk); // k x k
      // steps 6-8: convergence check
      Scalar normC = Matrix2Norm.bound(C);
      if (Tolerance.CHOP.isZero(normC))
        // prepare output
        return L.extract(0, k).dot(Join.of(p, Qt));
      // step 9
      Tensor phi = MatrixExp.of(C); // k x k
      // step 10
      Tensor B = V.block(l0k, List.of(V.length(), k)).dot(phi); // 2k x k
      V = Join.of(1, V.block(l00, List.of(V.length(), k)), B); // 2k x 2k
    }
    throw new Throw(p, q);
  }
}
