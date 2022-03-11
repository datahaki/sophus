// code by jph / ob
package ch.alpine.sophus.lie.so3;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.math.bch.BakerCampbellHausdorff;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.LowerTriangularize;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.UnitaryMatrixQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.pd.Orthogonalize;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.mat.qr.QRSignOperator;
import ch.alpine.tensor.mat.qr.QRSignOperators;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.VectorInfinityNorm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import junit.framework.TestCase;

public class RodriguesTest extends TestCase {
  public void testConvergenceSo3() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Rodrigues.vectorExp(x);
    Tensor mY = Rodrigues.vectorExp(y);
    Tensor res = Rodrigues.INSTANCE.vectorLog(mX.dot(mY));
    Tensor ad = N.DOUBLE.of(LeviCivitaTensor.of(3).negate());
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      BinaryOperator<Tensor> binaryOperator = BakerCampbellHausdorff.of(ad, degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  public void testSimple() {
    Tensor vector = Tensors.vector(0.2, 0.3, -0.4);
    Tensor m1 = Rodrigues.vectorExp(vector);
    Tensor m2 = Rodrigues.vectorExp(vector.negate());
    assertFalse(Chop._12.isClose(m1, IdentityMatrix.of(3)));
    Chop._12.requireClose(m1.dot(m2), IdentityMatrix.of(3));
  }

  public void testLog() {
    Tensor vector = Tensors.vector(0.2, 0.3, -0.4);
    Tensor matrix = Rodrigues.vectorExp(vector);
    Tensor result = Rodrigues.INSTANCE.vectorLog(matrix);
    assertEquals(result, vector);
  }

  public void testTranspose() {
    Tensor vector = Tensors.vector(Math.random(), Math.random(), -Math.random());
    Tensor m1 = Rodrigues.vectorExp(vector);
    Tensor m2 = Transpose.of(Rodrigues.vectorExp(vector));
    Chop._12.requireClose(IdentityMatrix.of(3), m1.dot(m2));
  }

  private static void checkDiff(Tensor c) {
    Tensor e = Rodrigues.vectorExp(c);
    Chop._14.requireClose(e, MatrixExp.of(Cross.skew3(c)));
    Chop._14.requireClose(e.dot(c), c);
  }

  public void testXY() {
    Tensor m22 = RotationMatrix.of(RealScalar.ONE);
    Tensor mat = Rodrigues.vectorExp(Tensors.vector(0, 0, 1));
    Tensor blu = Tensors.of( //
        mat.get(0).extract(0, 2), //
        mat.get(1).extract(0, 2));
    assertEquals(blu, m22);
  }

  public void testFormula() {
    checkDiff(Tensors.vector(-0.2, 0.1, 0.3));
    checkDiff(Tensors.vector(-0.5, -0.1, 0.03));
    checkDiff(Tensors.vector(-0.3, -0.2, 0.1));
    checkDiff(Tensors.vector(-0.3, -0.2, -0.3));
  }

  public void testRotZ() {
    Tensor matrix = Rodrigues.vectorExp(Tensors.vector(0, 0, 1));
    assertEquals(matrix.get(2, 0), RealScalar.ZERO);
    assertEquals(matrix.get(2, 1), RealScalar.ZERO);
    assertEquals(matrix.get(0, 2), RealScalar.ZERO);
    assertEquals(matrix.get(1, 2), RealScalar.ZERO);
    assertEquals(matrix.get(2, 2), RealScalar.ONE);
  }

  public void testPi() {
    Tensor matrix = Rodrigues.vectorExp(Tensors.vector(0, 0, Math.PI));
    Tensor expected = DiagonalMatrix.of(-1, -1, 1);
    Chop._14.requireClose(matrix, expected);
  }

  public void testTwoPi() {
    Tensor matrix = Rodrigues.vectorExp(Tensors.vector(0, 0, 2 * Math.PI));
    Tensor expected = DiagonalMatrix.of(1, 1, 1);
    Chop._14.requireClose(matrix, expected);
  }

  public void testLogEye() {
    Tensor matrix = IdentityMatrix.of(3);
    Tensor log = Rodrigues.INSTANCE.log(matrix);
    Chop.NONE.requireAllZero(log);
  }

  public void testLog1() {
    Tensor vec = Tensors.vector(.3, .5, -0.4);
    Tensor matrix = Rodrigues.vectorExp(vec);
    Tensor lom = Rodrigues.INSTANCE.log(matrix);
    Tensor log = Rodrigues.INSTANCE.vectorLog(matrix);
    Chop._14.requireClose(vec, log);
    Chop._14.requireClose(lom, Cross.skew3(vec));
  }

  public void testLogEps() {
    double v = 0.25;
    Tensor log;
    do {
      v = v / 1.1;
      Tensor vec = Tensors.vector(v, v, v);
      Tensor matrix = Rodrigues.vectorExp(vec);
      {
        Tensor logM = Rodrigues.INSTANCE.log(matrix);
        Chop._13.requireClose(logM.negate(), Transpose.of(logM));
      }
      log = Rodrigues.INSTANCE.log(matrix);
    } while (!Chop._20.allZero(log));
  }

  public void testLogEps2() {
    double eps = Double.MIN_VALUE; // 4.9e-324
    Tensor vec = Tensors.vector(eps, 0, 0);
    Tensor matrix = Rodrigues.vectorExp(vec);
    Tensor log = Rodrigues.INSTANCE.log(matrix);
    Chop._50.requireAllZero(log);
  }

  public void testRodriques() {
    for (int count = 0; count < 20; ++count) {
      Tensor matrix = So3TestHelper.spawn_So3();
      assertTrue(OrthogonalMatrixQ.of(matrix));
      OrthogonalMatrixQ.require(matrix);
    }
  }

  private static QRDecomposition specialOps(Tensor A) {
    QRDecomposition qrDecomposition = null;
    for (QRSignOperator qrSignOperator : QRSignOperators.values()) {
      qrDecomposition = QRDecomposition.of(A, qrSignOperator);
      Tensor Q = qrDecomposition.getQ();
      Tensor Qi = qrDecomposition.getQConjugateTranspose();
      Tensor R = qrDecomposition.getR();
      Chop._10.requireClose(Q.dot(R), A);
      Chop._10.requireClose(Q.dot(Qi), IdentityMatrix.of(A.length()));
      Scalar qrDet = Det.of(Q).multiply(Det.of(R));
      Chop._10.requireClose(qrDet, Det.of(A));
      Tensor lower = LowerTriangularize.of(R, -1);
      Tolerance.CHOP.requireAllZero(lower);
      Chop._10.requireClose(qrDet, qrDecomposition.det());
    }
    return qrDecomposition;
  }

  public void testRandomOrthogonal() {
    for (int count = 0; count < 5; ++count) {
      Tensor matrix = So3TestHelper.spawn_So3();
      specialOps(matrix);
      QRDecomposition qr = QRDecomposition.of(matrix, QRSignOperators.ORIENTATION);
      Chop._10.requireClose(qr.getR(), IdentityMatrix.of(3));
      Chop._12.requireClose(qr.getQ(), matrix);
    }
  }

  public void testRandomOrthogonal2() {
    Distribution noise = UniformDistribution.of(-0.03, 0.03);
    for (int count = 0; count < 5; ++count) {
      Tensor matrix = So3TestHelper.spawn_So3().add(RandomVariate.of(noise, 3, 3));
      specialOps(matrix);
      QRDecomposition qr = QRDecomposition.of(matrix, QRSignOperators.ORIENTATION);
      Scalar infNorm = VectorInfinityNorm.of(Diagonal.of(qr.getR()).map(s -> s.subtract(RealScalar.ONE)));
      assertTrue(Scalars.lessThan(infNorm, RealScalar.of(0.1)));
    }
  }

  public void testRodriguez() {
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 3);
    Tensor wedge = Cross.skew3(vector);
    Chop._13.requireClose(MatrixExp.of(wedge), Rodrigues.vectorExp(vector));
  }

  public void testRodriques2() {
    for (int c = 0; c < 20; ++c) {
      Tensor matrix = So3TestHelper.spawn_So3();
      assertTrue(UnitaryMatrixQ.of(matrix));
    }
  }

  public void testOrthPassFormatFailEye() {
    Scalar one = RealScalar.ONE;
    Tensor eyestr = Tensors.matrix((i, j) -> i.equals(j) ? one : one.zero(), 3, 4);
    AssertFail.of(() -> Rodrigues.INSTANCE.log(eyestr));
  }

  public void testOrthPassFormatFail2() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 5);
    Tensor orthog = Orthogonalize.of(matrix);
    assertTrue(OrthogonalMatrixQ.of(orthog));
    AssertFail.of(() -> Rodrigues.INSTANCE.log(orthog));
  }

  public void testOrthogonalize() {
    Tensor matrix = So3TestHelper.spawn_So3();
    Tolerance.CHOP.requireClose(Orthogonalize.of(matrix), matrix);
    Tolerance.CHOP.requireClose(Orthogonalize.usingSvd(matrix), matrix);
    Tolerance.CHOP.requireClose(Orthogonalize.usingPD(matrix), matrix);
  }

  public void testFail() {
    AssertFail.of(() -> Rodrigues.INSTANCE.exp(RealScalar.ZERO));
    AssertFail.of(() -> Rodrigues.INSTANCE.exp(Tensors.vector(0, 0)));
    AssertFail.of(() -> Rodrigues.INSTANCE.exp(Tensors.vector(0, 0, 0, 0)));
  }

  public void testLogTrash() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 3);
    AssertFail.of(() -> Rodrigues.INSTANCE.log(matrix));
  }

  public void testLogFail() {
    AssertFail.of(() -> Rodrigues.INSTANCE.log(Array.zeros(3)));
    AssertFail.of(() -> Rodrigues.INSTANCE.log(Array.zeros(3, 4)));
    AssertFail.of(() -> Rodrigues.INSTANCE.log(Array.zeros(3, 3, 3)));
  }
}
