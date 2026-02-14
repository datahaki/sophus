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

class Se3Exponential0Test {
  @Test
  void test() {
    Exponential exponential = Se3Exponential0.INSTANCE;
    ZeroDefectArrayQ zeroDefectArrayQ = exponential.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, 4, 4);
    assertEquals(linearSubspace.dimensions(), 6);
    Tensor weights = RandomVariate.of(NormalDistribution.standard(), 6);
    Tensor v = linearSubspace.apply(weights);
    Tensor p = exponential.exp(v);
    SeNGroup seNGroup = new SeNGroup(3);
    seNGroup.isPointQ().require(p);
    Tensor w = exponential.log(p);
    // IO.println(Dimensions.of(v));
    // IO.println(Dimensions.of(w));
    // IO.println(Pretty.of(v.maps(Round._3)));
    // IO.println(Pretty.of(w.maps(Round._3)));
    Tolerance.CHOP.requireClose(v, w);
  }
}
