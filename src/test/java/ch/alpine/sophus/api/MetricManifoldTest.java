// code by jph
package ch.alpine.sophus.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;

class MetricManifoldTest {
  static List<MetricManifold> metricManifolds() {
    return SophusExperimental.filter(MetricManifold.class);
  }

  @ParameterizedTest
  @MethodSource("metricManifolds")
  void testNon(MetricManifold metricManifold) {
    assumeTrue(metricManifold instanceof RandomSampleInterface);
    RandomSampleInterface rsi = (RandomSampleInterface) metricManifold;
    Tensor p = RandomSample.of(rsi);
    BilinearForm bilinearForm = metricManifold.bilinearForm(p);
    assertNotNull(bilinearForm);
  }
}
