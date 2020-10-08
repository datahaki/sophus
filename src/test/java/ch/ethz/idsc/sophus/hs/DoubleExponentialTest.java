// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.DoubleExponential.DoubleExponentialPoint;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import junit.framework.TestCase;

public class DoubleExponentialTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    DoubleExponential doubleExponential = //
        Serialization.copy(new DoubleExponential(SnManifold.INSTANCE, SnTransport.INSTANCE));
    DoubleExponentialPoint doubleExponentialPoint = doubleExponential.at(UnitVector.of(3, 0));
    TensorUnaryOperator operator = //
        Serialization.copy(doubleExponentialPoint.operator(UnitVector.of(3, 1).multiply(Pi.HALF)));
    Tensor tensor = operator.apply(UnitVector.of(3, 2).multiply(Pi.HALF));
    Tolerance.CHOP.requireClose(tensor, UnitVector.of(3, 2));
  }
}
