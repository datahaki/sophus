// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnExponential;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2BiinvariantMeans;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.math.Do;
import ch.alpine.sophus.math.TensorIteration;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.num.Derive;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Hermite3FilterTest extends TestCase {
  public void testR1PolynomialReproduction() {
    Tensor coeffs = Tensors.vector(1, 3, -2, 3);
    ScalarUnaryOperator f0 = Polynomial.of(coeffs);
    ScalarUnaryOperator f1 = Polynomial.of(Derive.of(coeffs));
    Tensor domain = Range.of(0, 10);
    Tensor control = Transpose.of(Tensors.of(domain.map(f0), domain.map(f1)));
    HermiteFilter hermiteFilter = //
        new Hermite3Filter(RnGroup.INSTANCE, RnExponential.INSTANCE, RnBiinvariantMean.INSTANCE);
    TensorIteration tensorIteration = hermiteFilter.string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 2);
    ExactTensorQ.require(iterate);
    assertEquals(control, iterate);
  }

  public void testSe2ConstantReproduction() {
    Tensor control = ConstantArray.of(Tensors.fromString("{{2, 3, 1}, {0, 0, 0}}"), 10);
    HermiteFilter hermiteFilter = //
        new Hermite3Filter(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE, Se2BiinvariantMeans.FILTER);
    TensorIteration tensorIteration = hermiteFilter.string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 2);
    Chop._14.requireClose(control, iterate);
  }

  public void testSe2LinearReproduction() {
    Tensor pg = Tensors.vector(1, 2, 3);
    Tensor pv = Tensors.vector(0.3, -0.2, -0.1);
    Tensor control = Tensors.empty();
    for (int count = 0; count < 10; ++count) {
      control.append(Tensors.of(pg, pv));
      pg = Se2Group.INSTANCE.element(pg).combine(Se2CoveringExponential.INSTANCE.exp(pv));
    }
    HermiteFilter hermiteFilter = //
        new Hermite3Filter(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE, Se2BiinvariantMeans.FILTER);
    TensorIteration tensorIteration = hermiteFilter.string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 2);
    Chop._14.requireClose(control, iterate);
  }
}
