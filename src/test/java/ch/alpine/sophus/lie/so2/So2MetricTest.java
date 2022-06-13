// code by gjoel
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;

class So2MetricTest {
  @Test
  void testTrivial1() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE, RealScalar.ONE);
    assertEquals(RealScalar.ZERO, err);
  }

  @Test
  void testTrivial2() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ZERO, Pi.TWO);
    assertEquals(RealScalar.ZERO, err);
  }

  @Test
  void testPositive() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE, RealScalar.of(2));
    assertEquals(RealScalar.ONE, err);
  }

  @Test
  void testNegative() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE.negate(), RealScalar.of(2).negate());
    assertEquals(RealScalar.ONE, err);
  }

  @Test
  void testMixed1() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE.negate(), RealScalar.ONE);
    assertEquals(RealScalar.of(2), err);
  }

  @Test
  void testMixed2() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE, Pi.TWO.subtract(RealScalar.ONE));
    assertEquals(RealScalar.of(2), err);
  }

  @Test
  void testMultiples() {
    Scalar err = So2Metric.INSTANCE.distance(Pi.VALUE.multiply(RealScalar.of(6)), RealScalar.ONE);
    assertEquals(RealScalar.ONE, err);
  }
}
