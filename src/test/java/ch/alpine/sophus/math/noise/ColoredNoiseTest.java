// code by jph
package ch.alpine.sophus.math.noise;

import java.util.DoubleSummaryStatistics;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.sca.Clips;

class ColoredNoiseTest {
  @Test
  void testSimple() {
    ColoredNoise coloredNoise = new ColoredNoise(1.2);
    DoubleSummaryStatistics doubleSummaryStatistics = //
        DoubleStream.generate(coloredNoise::nextValue) //
            .limit(1000).summaryStatistics();
    double average = doubleSummaryStatistics.getAverage();
    double min = doubleSummaryStatistics.getMin();
    double max = doubleSummaryStatistics.getMax();
    Clips.absoluteOne().requireInside(RealScalar.of(average));
    Clips.interval(-20, 0).requireInside(RealScalar.of(min));
    Clips.interval(0, +20).requireInside(RealScalar.of(max));
  }
}
