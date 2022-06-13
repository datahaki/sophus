// code by jph
package ch.alpine.sophus.math.noise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.BinCounts;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class SimplexContinuousNoiseTest {
  @Test
  void testSimple() {
    Tensor noise = Tensors.vector(i -> DoubleScalar.of(10 * (1 + SimplexContinuousNoise.FUNCTION.at(0.1 * i, 0.1 + i))), 1000);
    Tensor bins = BinCounts.of(noise);
    assertEquals(bins.length(), 20);
    long len = bins.stream() //
        .map(Scalar.class::cast) //
        .filter(scalar -> Scalars.lessThan(DoubleScalar.of(30), scalar)) //
        .count();
    assertTrue(10 < len);
  }

  @Test
  void testExample() {
    double value = SimplexContinuousNoise.FUNCTION.at(0.3, 300.3, -600.5);
    assertEquals(value, -0.12579872366423636);
  }

  @Test
  void testMulti1() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Clip clip = Clips.absoluteOne();
    for (int index = 0; index < 1000; ++index) {
      double vx = RandomVariate.of(distribution).number().doubleValue();
      double number = SimplexContinuousNoise.FUNCTION.at(vx);
      clip.requireInside(RealScalar.of(number));
    }
  }

  @Test
  void testMulti3() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Clip clip = Clips.absoluteOne();
    for (int index = 0; index < 1000; ++index) {
      double vx = RandomVariate.of(distribution).number().doubleValue();
      double vy = RandomVariate.of(distribution).number().doubleValue();
      double vz = RandomVariate.of(distribution).number().doubleValue();
      double number = SimplexContinuousNoise.FUNCTION.at(vx, vy, vz);
      clip.requireInside(RealScalar.of(number));
    }
  }

  @Test
  void testMulti4() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Clip clip = Clips.absoluteOne();
    for (int index = 0; index < 1000; ++index) {
      double vx = RandomVariate.of(distribution).number().doubleValue();
      double vy = RandomVariate.of(distribution).number().doubleValue();
      double vz = RandomVariate.of(distribution).number().doubleValue();
      double va = RandomVariate.of(distribution).number().doubleValue();
      double number = SimplexContinuousNoise.at(vx, vy, vz, va);
      clip.requireInside(RealScalar.of(number));
    }
  }
}
