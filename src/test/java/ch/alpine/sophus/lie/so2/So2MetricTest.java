// code by gjoel
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;

public class So2MetricTest {
  @Test
  public void testTrivial1() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE, RealScalar.ONE);
    assertEquals(RealScalar.ZERO, err);
  }

  @Test
  public void testTrivial2() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ZERO, Pi.TWO);
    assertEquals(RealScalar.ZERO, err);
  }

  @Test
  public void testPositive() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE, RealScalar.of(2));
    assertEquals(RealScalar.ONE, err);
  }

  @Test
  public void testNegative() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE.negate(), RealScalar.of(2).negate());
    assertEquals(RealScalar.ONE, err);
  }

  @Test
  public void testMixed1() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE.negate(), RealScalar.ONE);
    assertEquals(RealScalar.of(2), err);
  }

  @Test
  public void testMixed2() {
    Scalar err = So2Metric.INSTANCE.distance(RealScalar.ONE, Pi.TWO.subtract(RealScalar.ONE));
    assertEquals(RealScalar.of(2), err);
  }

  @Test
  public void testMultiples() {
    Scalar err = So2Metric.INSTANCE.distance(Pi.VALUE.multiply(RealScalar.of(6)), RealScalar.ONE);
    assertEquals(RealScalar.ONE, err);
  }
}
