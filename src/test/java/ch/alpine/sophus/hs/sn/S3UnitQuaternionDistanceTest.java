// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.lie.Quaternion;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Log;

class S3UnitQuaternionDistanceTest {
  @Test
  void testSimple() {
    Quaternion p = Quaternion.of(3, 1, 2, 3);
    p = p.divide(Abs.FUNCTION.apply(p));
    Quaternion q = Quaternion.of(-2, 0, -4, 7);
    q = q.divide(Abs.FUNCTION.apply(q));
    Chop._14.requireClose(Abs.FUNCTION.apply(p), RealScalar.ONE);
    Chop._14.requireClose(Abs.FUNCTION.apply(q), RealScalar.ONE);
    Scalar d1 = S3UnitQuaternionDistance.INSTANCE.distance(p, q);
    Scalar dq = S3UnitQuaternionDistance.INSTANCE.distance(p.multiply(q), q.multiply(q));
    Scalar dp = S3UnitQuaternionDistance.INSTANCE.distance(p.multiply(p), p.multiply(q));
    Chop._14.requireClose(d1, dp);
    Chop._14.requireClose(d1, dq);
    Chop._14.requireAllZero(S3UnitQuaternionDistance.INSTANCE.distance(p, p));
    Scalar distance = S3LogUnitQuaternionDistance.INSTANCE.distance(p, q);
    Chop._14.requireClose(dp, distance);
    Chop._14.requireClose(dq, distance);
  }

  @Test
  void testEichen() {
    Quaternion p0 = Quaternion.of(1, 0, 0, 0);
    Quaternion p1 = Quaternion.of(0, 1, 0, 0);
    Quaternion p2 = Quaternion.of(0, 0, 1, 0);
    Quaternion p3 = Quaternion.of(0, 0, 0, 1);
    Scalar d01a = S3UnitQuaternionDistance.INSTANCE.distance(p0, p1);
    Chop._14.requireClose(d01a, Pi.HALF);
    Scalar d01b = S3LogUnitQuaternionDistance.INSTANCE.distance(p0, p1);
    Chop._14.requireClose(d01a, d01b);
    Scalar d23a = S3UnitQuaternionDistance.INSTANCE.distance(p2, p3);
    Scalar d23b = S3LogUnitQuaternionDistance.INSTANCE.distance(p2, p3);
    Chop._14.requireClose(d23a, d23b);
    assertEquals(Abs.FUNCTION.apply(p0), RealScalar.ONE);
    assertEquals(Abs.FUNCTION.apply(p1), RealScalar.ONE);
    assertEquals(Abs.FUNCTION.apply(p2), RealScalar.ONE);
    assertEquals(Abs.FUNCTION.apply(p3), RealScalar.ONE);
  }

  @Test
  void testQuaternionLogExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 30; ++index) {
      Quaternion quaternion = Quaternion.of(RandomVariate.of(distribution), RandomVariate.of(distribution, 3));
      Scalar log = Log.FUNCTION.apply(quaternion);
      Scalar exp = Exp.FUNCTION.apply(log);
      Chop._14.requireClose(quaternion, exp);
    }
  }

  @Test
  void testQuaternionExpLog() {
    Distribution distribution = NormalDistribution.of(0, .3);
    for (int index = 0; index < 30; ++index) {
      Quaternion quaternion = Quaternion.of(RandomVariate.of(distribution), RandomVariate.of(distribution, 3));
      Scalar exp = Exp.FUNCTION.apply(quaternion);
      Scalar log = Log.FUNCTION.apply(exp);
      Chop._14.requireClose(quaternion, log);
    }
  }
}
