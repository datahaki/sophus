// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.GroupCheck;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.qr.SchurDecomposition;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class So3GroupTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(So3Group.INSTANCE);
    GroupCheck.check(So3Group.INSTANCE);
  }

  @Test
  void testAd() {
    Tensor ad = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis()).ad();
    assertEquals(ad, LeviCivitaTensor.of(3).negate());
  }

  @Test
  void testConsistent() {
    SoNGroup soNGroup = new SoNGroup(3);
    for (var elem : soNGroup.matrixBasis()) {
      Tensor exp1 = So3Group.INSTANCE.exponential0().exp(elem);
      Tensor exp2 = soNGroup.exponential0().exp(elem);
      Tolerance.CHOP.requireClose(exp1, exp2);
    }
  }

  @Test
  void testAdjoint() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      Tensor v = So3TestHelper.spawn_so3();
      Tensor tensor = LinearSolve.of(g, v).dot(g);
      AntisymmetricMatrixQ.INSTANCE.requireMember(tensor);
      // AntisymmetricMatrixQ.require(So3Exponential.INSTANCE.log(g));
      // VectorQ.requireLength(So3Group.INSTANCE.exponential0().vectorLog(g), 3);
    }
  }

  @Test
  void testLinearGroup() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      // SoGroupElement so3GroupElement = So3Group.INSTANCE.element(g);
      Tensor v = So3TestHelper.spawn_so3();
      Tolerance.CHOP.requireClose( //
          So3Group.INSTANCE.adjoint(g, v), //
          GlGroup.INSTANCE.adjoint(g, v));
      Tolerance.CHOP.requireClose( //
          So3Group.INSTANCE.dL(g, v), //
          GlGroup.INSTANCE.dL(g, v));
    }
  }

  @Test
  void testSimple2() {
    Tensor p = Rodrigues.vectorExp(Tensors.vector(1, 2, 3));
    Tensor q = Rodrigues.vectorExp(Tensors.vector(2, -1, 2));
    Tensor split = So3Group.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(new OrthogonalMatrixQ(Chop._14).isMember(split));
  }

  @Test
  void testEndPoints() {
    Distribution distribution = NormalDistribution.of(0, .3);
    for (int index = 0; index < 10; ++index) {
      Tensor p = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Tensor q = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Chop._14.requireClose(p, So3Group.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._11.requireClose(q, So3Group.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }

  @Test
  void testFailOrthogonal() {
    assertThrows(Exception.class, () -> So3Group.INSTANCE.exponential0().log(So3TestHelper.spawn_so3()));
  }

  @Test
  void testDistance() {
    Tensor vector = Tensors.vector(0.2, 0.5, 0.3);
    Scalar distance = So3Group.INSTANCE.distance( //
        Rodrigues.vectorExp(Tensors.vector(0, 0, 0)), //
        Rodrigues.vectorExp(vector));
    Chop._15.requireClose(distance, Vector2Norm.of(vector));
  }

  @Test
  void test4x4Fail() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor p = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    Tensor q = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    OrthogonalMatrixQ.INSTANCE.requireMember(p);
    assertThrows(Exception.class, () -> So3Group.INSTANCE.distance(p, q));
  }

  @RepeatedTest(10)
  void testSimple3() {
    Tensor matrix = RandomSample.of(So3Group.INSTANCE);
    MatrixQ.requireSize(matrix, 3, 3);
    OrthogonalMatrixQ.INSTANCE.requireMember(matrix);
    Tolerance.CHOP.requireClose(Det.of(matrix), RealScalar.ONE);
    SchurDecomposition schurDecomposition = SchurDecomposition.of(matrix);
    schurDecomposition.getT();
  }
}
