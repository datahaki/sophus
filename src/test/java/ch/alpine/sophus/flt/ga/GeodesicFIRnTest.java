// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.red.Mean;

public class GeodesicFIRnTest {
  @Test
  public void testSimple() {
    Tensor s0 = Tensors.vector(1, 2, 3);
    Tensor s1 = Tensors.vector(2, 2, 0);
    Tensor s2 = Tensors.vector(3, 3, 3);
    Tensor s3 = Tensors.vector(9, 3, 2);
    TensorUnaryOperator tensorUnaryOperator = GeodesicFIRn.of(Mean::of, RnGeodesic.INSTANCE, 2, RationalScalar.of(1, 2));
    tensorUnaryOperator.apply(s0);
    tensorUnaryOperator.apply(s1);
    tensorUnaryOperator.apply(s2);
    Tensor tensor = tensorUnaryOperator.apply(s3);
    Tensor p = Mean.of(Tensors.of(s1, s2));
    Tensor q = s3;
    assertEquals(tensor, RnGeodesic.INSTANCE.split(p, q, RationalScalar.HALF));
    ExactTensorQ.require(tensor);
  }
}
