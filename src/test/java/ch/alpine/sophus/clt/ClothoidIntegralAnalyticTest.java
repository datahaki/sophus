// code by jph
package ch.alpine.sophus.clt;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
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

  @Test
  void testSimple1() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator clothoidPartial = Serialization.copy(ClothoidIntegralAnalytic.of(1.3, -0.7, 0.2));
    Scalar result = clothoidPartial.apply(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.1082616101141145, 0.27929038997053457);
    Tolerance.CHOP.requireClose(result, expect);
  }

  @Test
  void testSimple2() {
    ScalarUnaryOperator clothoidPartial = ClothoidIntegralAnalytic.of(1.5, 0.3, -0.4);
    Scalar result = clothoidPartial.apply(RealScalar.of(0.4));
    Scalar expect = ComplexScalar.of(0.012847600349171472, 0.39973677660732543);
    Tolerance.CHOP.requireClose(result, expect);
  }

  @Test
  void testCircle() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator clothoidPartial = Serialization.copy(ClothoidIntegralAnalytic.of(1.3, -0.7, 0));
    Scalar result = clothoidPartial.apply(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.10990181566815083, 0.2785521975010192);
    Tolerance.CHOP.requireClose(result, expect);
  }

  @Test
  void testLine() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator clothoidPartial = Serialization.copy(ClothoidIntegralAnalytic.of(1.3, 0, 0));
    Scalar result = clothoidPartial.apply(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.0802496485873762, 0.2890674556251579);
    Tolerance.CHOP.requireClose(result, expect);
  }
}
