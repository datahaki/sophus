// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3InverseDistanceCoordinatesTest extends TestCase {
  public void testSimple() {
    Tensor g1 = So3Exponential.INSTANCE.exp(Tensors.vector(0.2, 0.3, 0.4));
    Tensor g2 = So3Exponential.INSTANCE.exp(Tensors.vector(0.1, 0.0, 0.5));
    Tensor g3 = So3Exponential.INSTANCE.exp(Tensors.vector(0.3, 0.5, 0.2));
    Tensor g4 = So3Exponential.INSTANCE.exp(Tensors.vector(0.5, 0.2, 0.1));
    Tensor sequence = Tensors.of(g1, g2, g3, g4);
    TensorUnaryOperator tensorUnaryOperator = So3InverseDistanceCoordinates.INSTANCE.of(sequence);
    Tensor mean = So3Exponential.INSTANCE.exp(Tensors.vector(0.4, 0.2, 0.3));
    Tensor weights = tensorUnaryOperator.apply(mean);
    Tensor defect = So3BiinvariantMeanDefect.INSTANCE.evaluate(sequence, weights, mean);
    Chop._10.requireAllZero(defect);
  }
}
