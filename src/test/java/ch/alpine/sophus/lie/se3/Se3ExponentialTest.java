// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class Se3ExponentialTest {
  @Test
  void test() {
    Exponential exponential = Se3Exponential.INSTANCE;
    ZeroDefectArrayQ zeroDefectArrayQ = exponential.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, 4, 4);
    assertEquals(linearSubspace.dimensions(), 6);
    Tensor weights = RandomVariate.of(NormalDistribution.of(0, 0.1), 6);
    Tensor v = linearSubspace.apply(weights);
    Tensor p = exponential.exp(v);
    SeNGroup seNGroup = new SeNGroup(3);
    seNGroup.isPointQ().require(p);
    Tensor w = exponential.log(p);
    Tolerance.CHOP.requireClose(v, w);
  }
}
