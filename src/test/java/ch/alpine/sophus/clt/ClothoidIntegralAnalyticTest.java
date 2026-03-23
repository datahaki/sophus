// code by jph
package ch.alpine.sophus.clt;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;

class ClothoidIntegralAnalyticTest {
  @Test
  void test1() {
    Scalar _N1_1_4 = ComplexScalar.of(+0.7071067811865476, 0.7071067811865475);
    Tolerance.CHOP.requireClose(_N1_1_4, ComplexScalar.unit(Pi.QUARTER));
  }

  @Test
  void test2() {
    Scalar _N1_3_4 = ComplexScalar.of(-0.7071067811865475, 0.7071067811865476);
    Tolerance.CHOP.requireClose(_N1_3_4, ComplexScalar.unit(Pi._3_4));
  }
}
