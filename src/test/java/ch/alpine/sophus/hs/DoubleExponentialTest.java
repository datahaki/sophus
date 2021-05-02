// code by jph
package ch.alpine.sophus.hs;

import java.io.IOException;

import ch.alpine.sophus.hs.DoubleExponential.DoubleExponentialPoint;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
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
