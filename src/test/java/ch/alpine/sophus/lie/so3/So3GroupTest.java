// code by jph
package ch.alpine.sophus.lie.so3;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.gl.GlGroupElement;
import ch.alpine.sophus.lie.so.SoGroupElement;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class So3GroupTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(So3Group.INSTANCE);
  }

  @Test
  void testAdjoint() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      Tensor v = So3TestHelper.spawn_so3();
      Tensor tensor = LinearSolve.of(g, v).dot(g);
      AntisymmetricMatrixQ.require(tensor);
      // AntisymmetricMatrixQ.require(So3Exponential.INSTANCE.log(g));
      VectorQ.requireLength(So3Group.INSTANCE.vectorLog(g), 3);
    }
  }

  @Test
  void testLinearGroup() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      SoGroupElement so3GroupElement = So3Group.INSTANCE.element(g);
      GlGroupElement linearGroupElement = GlGroup.INSTANCE.element(g);
      Tensor v = So3TestHelper.spawn_so3();
      Tolerance.CHOP.requireClose( //
          so3GroupElement.adjoint(v), //
          linearGroupElement.adjoint(v));
      Tolerance.CHOP.requireClose( //
          so3GroupElement.dL(v), //
          linearGroupElement.dL(v));
    }
  }

  @Test
  void testSimple2() {
    Tensor p = Rodrigues.vectorExp(Tensors.vector(1, 2, 3));
    Tensor q = Rodrigues.vectorExp(Tensors.vector(2, -1, 2));
    Tensor split = So3Group.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(OrthogonalMatrixQ.of(split, Chop._14));
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
    assertThrows(Exception.class, () -> So3Group.INSTANCE.log(So3TestHelper.spawn_so3()));
  }
}
