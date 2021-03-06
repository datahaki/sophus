// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2UniformResampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = Tensors.of(Tensors.vector(5, 0, Math.PI / 2), Tensors.vector(5, 9, Math.PI / 2), Tensors.vector(5, 12, Math.PI / 2));
    CurveSubdivision curveSubdivision = Se2UniformResample.of(RealScalar.of(2));
    Tensor uniform = curveSubdivision.string(tensor);
    Chop._12.requireClose(uniform.get(Tensor.ALL, 0), ConstantArray.of(RealScalar.of(5), 6));
    Chop._12.requireClose(uniform.get(Tensor.ALL, 1), Subdivide.of(0, 10, 5));
    Chop._12.requireClose(uniform.get(Tensor.ALL, 2), ConstantArray.of(Pi.HALF, 6));
  }

  public void testNullFail() {
    AssertFail.of(() -> Se2UniformResample.of(null));
  }
}
