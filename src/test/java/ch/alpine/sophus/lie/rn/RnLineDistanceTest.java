// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.decim.HsLineDistance;
import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class RnLineDistanceTest {
  @Test
  void testSimple() {
    LineDistance lineDistance = RnLineDistance.INSTANCE;
    TensorNorm tensorNorm = lineDistance.tensorNorm(Tensors.vector(10, 0), Tensors.vector(10, 20));
    Scalar norm = tensorNorm.norm(Tensors.vector(30, 100));
    ExactScalarQ.require(norm);
    assertEquals(norm, RealScalar.of(20));
  }

  @Test
  void testConsistent() {
    LineDistance lineDistance1 = RnLineDistance.INSTANCE;
    LineDistance lineDistance2 = new HsLineDistance(RnGroup.INSTANCE);
    Distribution distribution = NormalDistribution.of(3, 2);
    for (int d = 2; d < 20; ++d) {
      Tensor p = RandomVariate.of(distribution, d);
      Tensor q = RandomVariate.of(distribution, d);
      Tensor r = RandomVariate.of(distribution, d);
      Scalar d1 = lineDistance1.tensorNorm(p, q).norm(r);
      Scalar d2 = lineDistance2.tensorNorm(p, q).norm(r);
      Tolerance.CHOP.requireClose(d1, d2);
    }
  }
}
