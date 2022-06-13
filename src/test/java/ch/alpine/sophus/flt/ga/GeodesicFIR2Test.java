// code by ob
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.flt.CausalFilter;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class GeodesicFIR2Test {
  @Test
  void testTranslation() throws ClassNotFoundException, IOException {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(1, 1, 0);
    Tensor r = Tensors.vector(1, 2, 1);
    Scalar alpha = RealScalar.of(0.5);
    Tensor control = Tensors.of(p, q, r);
    TensorUnaryOperator geodesicFIR2 = //
        Serialization.copy(GeodesicFIR2.of(Se2Group.INSTANCE, alpha));
    Tensor refined = Tensor.of(control.stream().map(geodesicFIR2));
    assertEquals(refined.get(1), Tensors.vector(1, 1, 0));
    Chop._12.requireClose(refined.get(2), Tensors.vector(1.5, 2.127670960610518, 0.5));
  }

  @Test
  void testRotation() {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(0, 0, 2);
    Scalar alpha = RealScalar.of(0.5);
    Tensor control = Tensors.of(p, q);
    TensorUnaryOperator geodesicFIR2 = GeodesicFIR2.of(Se2Group.INSTANCE, alpha);
    Tensor refined = Tensor.of(control.stream().map(geodesicFIR2));
    assertEquals(refined.get(1), Tensors.vector(0, 0, 2));
  }

  @Test
  void testCombined() {
    Scalar alpha = RealScalar.of(0.5);
    TensorUnaryOperator causalFilter = //
        CausalFilter.of(() -> GeodesicFIR2.of(Se2Group.INSTANCE, alpha));
    Distribution distribution = NormalDistribution.standard();
    Tensor control = RandomVariate.of(distribution, 100, 3);
    Tensor result = causalFilter.apply(control);
    assertEquals(Dimensions.of(result), Arrays.asList(100, 3));
    control.set(Scalar::zero, 0, Tensor.ALL);
    Tensor passtw = causalFilter.apply(control);
    assertFalse(Chop._11.isClose(result.get(0), passtw.get(0)));
    assertEquals(result.get(1), passtw.get(1));
    assertFalse(Chop._11.isClose(result.get(2), passtw.get(2)));
    assertEquals(result.get(3), passtw.get(3));
    assertEquals(result.get(4), passtw.get(4));
    Tensor lr = Last.of(result);
    Tensor lp = Last.of(passtw);
    assertEquals(lr, lp);
  }
}
