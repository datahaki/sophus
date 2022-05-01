// code by jph
package ch.alpine.sophus.hs.rs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class RnSBezierSplitTest {
  @ParameterizedTest
  @EnumSource(RnSBezierSplit.class)
  void test(RnSBezierSplit rnSBezierSplit) {
    Tensor pv0 = Tensors.fromString("{{0,0},{0,1}}");
    Tensor pv1 = Tensors.fromString("{{2,0},{0,1}}");
    rnSBezierSplit.split(pv0, pv1, RealScalar.of(0.5));
  }
}
