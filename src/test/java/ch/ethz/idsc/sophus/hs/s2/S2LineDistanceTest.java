// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class S2LineDistanceTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    TensorNorm tensorNorm = Serialization.copy(new S2LineDistance(Tensors.vector(1, 0, 0), Tensors.vector(0, 1, 0)));
    Chop._12.requireZero(tensorNorm.norm(Tensors.vector(-1, 0, 0)));
    Chop._12.requireClose(tensorNorm.norm(Tensors.vector(0, 0, +1)), Pi.HALF);
    Chop._12.requireClose(tensorNorm.norm(Tensors.vector(0, 0, -1)), Pi.HALF);
  }
}
