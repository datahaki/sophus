// code by jph
package ch.alpine.sophus.lie.se;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.se3.Se3Exponential0;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class SeExponential0Test {
  @Test
  void test() {
    Exponential exp1 = Se3Exponential0.INSTANCE;
    ZeroDefectArrayQ zeroDefectArrayQ = exp1.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, 4, 4);
    assertEquals(linearSubspace.dimensions(), 6);
    Tensor weights = RandomVariate.of(NormalDistribution.standard(), 6);
    Tensor v = linearSubspace.apply(weights);
    Tensor p1 = exp1.exp(v);
    Exponential exp2 = Se3Exponential0.INSTANCE;
    Tensor p2 = exp2.exp(v);
    Tolerance.CHOP.requireClose(p1, p2);
    Tensor w1 = exp1.log(p1);
    Tensor w2 = exp2.log(p2);
    Tolerance.CHOP.requireClose(w1, w2);
  }
}
