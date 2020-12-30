// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dot;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import junit.framework.TestCase;

public class CliffordAlgebraTest extends TestCase {
  public void testD2() {
    Tensor gp = CliffordAlgebra.of(2);
    Tensor x = Tensors.vector(1, 2, 3, 4);
    Tensor m = gp.dot(x);
    LinearSolve.of(m, UnitVector.of(4, 0));
    Tensor tensor = Dot.of(gp, Tensors.vector(0, 1, 0, 0), Tensors.vector(0, 0, 1, 0));
    assertEquals(tensor, UnitVector.of(4, 3));
    Tensor res = TensorWedge.of(Tensors.vector(1, 0), Tensors.vector(0, 1));
    assertEquals(res, Tensors.fromString("{{0, 1}, {-1, 0}}"));
  }

  public void testD3() {
    Tensor gp = CliffordAlgebra.of(3);
    Tensor x = Tensors.vector(1, 2, 3, 4, 5, -3, -4, -1);
    Tensor m = gp.dot(x);
    LinearSolve.of(m, UnitVector.of(8, 0));
  }
}
