// code by jph
package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.sca.Chop;

class SphereRandomSampleTest {
  @RepeatedTest(5)
  void testSimple(RepetitionInfo repetitionInfo) {
    int dimension = repetitionInfo.getCurrentRepetition();
    RandomSampleInterface randomSampleInterface = new Sphere(dimension);
    Tensor tensor = RandomSample.of(randomSampleInterface);
    Chop._12.requireClose(Vector2Norm.of(tensor), RealScalar.ONE);
    assertEquals(tensor.length(), dimension + 1);
  }

  @Test
  void testSNegFail() {
    assertThrows(Exception.class, () -> new Sphere(-1));
  }
}
