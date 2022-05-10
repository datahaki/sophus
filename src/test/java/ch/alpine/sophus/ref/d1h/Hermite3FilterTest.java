// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2BiinvariantMeans;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.Do;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.sca.Chop;

class Hermite3FilterTest {
  @Test
  public void testR1PolynomialReproduction() {
    Tensor coeffs = Tensors.vector(1, 3, -2, 3);
    Polynomial f0 = Polynomial.of(coeffs);
    Polynomial f1 = f0.derivative();
    Tensor domain = Range.of(0, 10);
    Tensor control = Transpose.of(Tensors.of(domain.map(f0), domain.map(f1)));
    HermiteFilter hermiteFilter = //
        new Hermite3Filter(RnGroup.INSTANCE, RnBiinvariantMean.INSTANCE);
    TensorIteration tensorIteration = hermiteFilter.string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 2);
    ExactTensorQ.require(iterate);
    assertEquals(control, iterate);
  }

  @Test
  public void testSe2ConstantReproduction() {
    Tensor control = ConstantArray.of(Tensors.fromString("{{2, 3, 1}, {0, 0, 0}}"), 10);
    HermiteFilter hermiteFilter = //
        new Hermite3Filter(Se2Group.INSTANCE, Se2BiinvariantMeans.FILTER);
    TensorIteration tensorIteration = hermiteFilter.string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 2);
    Chop._14.requireClose(control, iterate);
  }

  @Test
  public void testSe2LinearReproduction() {
    Tensor pg = Tensors.vector(1, 2, 3);
    Tensor pv = Tensors.vector(0.3, -0.2, -0.1);
    Tensor control = Tensors.empty();
    for (int count = 0; count < 10; ++count) {
      control.append(Tensors.of(pg, pv));
      pg = Se2Group.INSTANCE.element(pg).combine(Se2CoveringGroup.INSTANCE.exp(pv));
    }
    HermiteFilter hermiteFilter = //
        new Hermite3Filter(Se2Group.INSTANCE, Se2BiinvariantMeans.FILTER);
    TensorIteration tensorIteration = hermiteFilter.string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 2);
    Chop._14.requireClose(control, iterate);
  }
}
