// code by jph / ob
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.so2.RotationMatrix;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.Orthogonalize;
import ch.ethz.idsc.tensor.lie.QRDecomposition;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LowerTriangularize;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.mat.UnitaryMatrixQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Decrement;
import junit.framework.TestCase;

public class RodriguesTest extends TestCase {
  public void testSimple() {
    Tensor vector = Tensors.vector(0.2, 0.3, -0.4);
    Tensor m1 = Rodrigues.vectorExp(vector);
    Tensor m2 = Rodrigues.vectorExp(vector.negate());
    assertFalse(Chop._12.close(m1, IdentityMatrix.of(3)));
    Chop._12.requireClose(m1.dot(m2), IdentityMatrix.of(3));
  }

  public void testLog() {
    Tensor vector = Tensors.vector(0.2, 0.3, -0.4);
    Tensor matrix = Rodrigues.vectorExp(vector);
    Tensor result = Rodrigues.INSTANCE.flattenLog(matrix);
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
    assertTrue(Chop.NONE.allZero(log));
  }

  public void testLog1() {
    Tensor vec = Tensors.vector(.3, .5, -0.4);
    Tensor matrix = Rodrigues.vectorExp(vec);
    Tensor lom = Rodrigues.INSTANCE.log(matrix);
    Tensor log = Rodrigues.INSTANCE.flattenLog(matrix);
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
    assertTrue(Chop._50.allZero(log));
  }

  public void testRodriques() {
    for (int count = 0; count < 20; ++count) {
      Tensor matrix = TestHelper.spawn_So3();
      assertTrue(OrthogonalMatrixQ.of(matrix));
      OrthogonalMatrixQ.require(matrix);
    }
  }

  private static QRDecomposition specialOps(Tensor A) {
    QRDecomposition qrDecomposition = QRDecomposition.of(A);
    Tensor Q = qrDecomposition.getQ();
    Tensor Qi = qrDecomposition.getInverseQ();
    Tensor R = qrDecomposition.getR();
    Chop._10.requireClose(Q.dot(R), A);
    Chop._10.requireClose(Q.dot(Qi), IdentityMatrix.of(A.length()));
    Scalar qrDet = Det.of(Q).multiply(Det.of(R));
    Chop._10.requireClose(qrDet, Det.of(A));
    Tensor lower = LowerTriangularize.of(R, -1);
    assertTrue(Chop.NONE.allZero(lower));
    Chop._10.requireClose(qrDet, qrDecomposition.det());
    return qrDecomposition;
  }

  public void testRandomOrthogonal() {
    for (int count = 0; count < 5; ++count) {
      Tensor matrix = TestHelper.spawn_So3();
      specialOps(matrix);
      QRDecomposition qr = QRDecomposition.preserveOrientation(matrix);
      Chop._13.requireClose(qr.getR(), IdentityMatrix.of(3));
      Chop._12.requireClose(qr.getQ(), matrix);
    }
  }

  public void testRandomOrthogonal2() {
    Distribution noise = UniformDistribution.of(-0.03, 0.03);
    for (int count = 0; count < 5; ++count) {
      Tensor matrix = TestHelper.spawn_So3().add(RandomVariate.of(noise, 3, 3));
      specialOps(matrix);
      QRDecomposition qr = QRDecomposition.preserveOrientation(matrix);
      Scalar infNorm = Norm.INFINITY.ofVector(Diagonal.of(qr.getR()).map(Decrement.ONE));
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
      Tensor matrix = TestHelper.spawn_So3();
      assertTrue(UnitaryMatrixQ.of(matrix));
    }
  }

  public void testOrthPassFormatFailEye() {
    Scalar one = RealScalar.ONE;
    Tensor eyestr = Tensors.matrix((i, j) -> i.equals(j) ? one : one.zero(), 3, 4);
    try {
      Rodrigues.INSTANCE.log(eyestr);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testOrthPassFormatFail2() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 5);
    Tensor orthog = Orthogonalize.of(matrix);
    assertTrue(OrthogonalMatrixQ.of(orthog));
    try {
      Rodrigues.INSTANCE.log(orthog);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testOrthogonalize() {
    for (int count = 0; count < 10; ++count) {
      Tensor matrix = TestHelper.spawn_So3();
      Tolerance.CHOP.requireClose(Orthogonalize.of(matrix), matrix);
    }
  }

  public void testFail() {
    try {
      Rodrigues.INSTANCE.exp(RealScalar.ZERO);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Rodrigues.INSTANCE.exp(Tensors.vector(0, 0));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Rodrigues.INSTANCE.exp(Tensors.vector(0, 0, 0, 0));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testLogTrash() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 3, 3);
    try {
      Rodrigues.INSTANCE.log(matrix);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testLogFail() {
    try {
      Rodrigues.INSTANCE.log(Array.zeros(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Rodrigues.INSTANCE.log(Array.zeros(3, 4));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Rodrigues.INSTANCE.log(Array.zeros(3, 3, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}