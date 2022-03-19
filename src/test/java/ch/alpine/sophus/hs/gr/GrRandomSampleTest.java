// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;

public class GrRandomSampleTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int k = 1; k < 5; ++k) {
      RandomSampleInterface grRandomSample = Serialization.copy(new GrRandomSample(k + 3, k));
      Tensor x = RandomSample.of(grRandomSample);
      GrMemberQ.INSTANCE.require(x);
    }
  }

  @Test
  public void testNN() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(new GrRandomSample(n, n));
      Tensor x = RandomSample.of(randomSampleInterface);
      GrMemberQ.INSTANCE.require(x);
      Chop._09.requireClose(x, IdentityMatrix.of(n));
    }
  }

  @Test
  public void testN0() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, 0);
      for (int k = 0; k < 3; ++k) {
        Tensor x = RandomSample.of(randomSampleInterface);
        GrMemberQ.INSTANCE.require(x);
        assertEquals(x, Array.zeros(n, n));
        x.set(RealScalar.ONE::add, Tensor.ALL, Tensor.ALL);
      }
    }
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> new GrRandomSample(-3, 2));
    assertThrows(Exception.class, () -> new GrRandomSample(3, 4));
  }
}
