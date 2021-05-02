// code by jph
package ch.alpine.sophus.lie.gl;

import java.io.IOException;

import ch.alpine.sophus.lie.se2.Se2GroupElement;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class GlGroupElementTest extends TestCase {
  public void testSimple() {
    int n = 5;
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), n, n);
    GlGroupElement linearGroupElement = GlGroupElement.of(matrix);
    Tensor result = linearGroupElement.inverse().combine(matrix);
    Chop._10.requireClose(result, IdentityMatrix.of(n));
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    Tensor tensor = DiagonalMatrix.of(1, 2, 3);
    GlGroupElement linearGroupElement = GlGroupElement.of(tensor);
    GlGroupElement copy = Serialization.copy(linearGroupElement);
    Tensor result = copy.inverse().combine(tensor);
    assertEquals(result, IdentityMatrix.of(3));
  }

  public void testLinearGroupSe2() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor adjoint = new Se2GroupElement(g).adjoint(uvw);
      GlGroupElement linearGroupElement = GlGroupElement.of(Se2Matrix.of(g));
      assertEquals(linearGroupElement.toCoordinate(), Se2Matrix.of(g));
      Tensor X = Tensors.matrix(new Scalar[][] { //
          { RealScalar.ZERO, uvw.Get(2).negate(), uvw.Get(0) }, //
          { uvw.Get(2), RealScalar.ZERO, uvw.Get(1) }, //
          { RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO } });
      Tensor tensor = linearGroupElement.adjoint(X);
      Tensor xya = Tensors.of(tensor.Get(0, 2), tensor.Get(1, 2), tensor.Get(1, 0));
      Chop._12.requireClose(adjoint, xya);
    }
  }

  public void testAdjoint() {
    Tensor xya = Tensors.vector(1, 2, 3);
    Se2GroupElement se2GroupElement = new Se2GroupElement(xya);
    Tensor matrix = Se2Matrix.of(xya);
    Tensor adjointGl = GlGroupElement.of(matrix).adjoint(Tensors.fromString("{{0, 1, 0}, {-1, 0, 0}, {0, 0, 0}}")).map(Chop._10);
    Tensor adjointSe = se2GroupElement.adjoint(Tensors.vector(0, 0, -1));
    Chop._12.requireClose(adjointGl.get(0, 2), adjointSe.get(0));
    Chop._12.requireClose(adjointGl.get(1, 2), adjointSe.get(1));
    Chop._12.requireClose(adjointGl.get(1, 0), adjointSe.get(2));
  }

  public void testAdjointFail() {
    GlGroupElement linearGroupElement = GlGroupElement.of(IdentityMatrix.of(5));
    AssertFail.of(() -> linearGroupElement.adjoint(Tensors.vector(1, 2, 3, 4, 5)));
  }

  public void testNonSquareFail() {
    AssertFail.of(() -> GlGroupElement.of(HilbertMatrix.of(2, 3)));
  }

  public void testCombineNonSquareFail() {
    GlGroupElement linearGroupElement = GlGroupElement.of(DiagonalMatrix.of(1, 2));
    AssertFail.of(() -> linearGroupElement.combine(HilbertMatrix.of(2, 3)));
    AssertFail.of(() -> linearGroupElement.combine(Tensors.vector(1, 2)));
  }

  public void testNonInvertibleFail() {
    AssertFail.of(() -> GlGroupElement.of(DiagonalMatrix.of(1, 0, 2)));
  }
}
