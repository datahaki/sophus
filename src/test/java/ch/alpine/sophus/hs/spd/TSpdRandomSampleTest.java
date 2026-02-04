// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.c.TriangularDistribution;

class TSpdRandomSampleTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    TSpdRandomSample simRandomSample = new TSpdRandomSample(3, TriangularDistribution.with(0, 1));
    Serialization.copy(simRandomSample);
    SymmetricMatrixQ.INSTANCE.requireMember(RandomSample.of(simRandomSample));
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new TSpdRandomSample(0, TriangularDistribution.with(0, 1)));
    assertThrows(Exception.class, () -> new TSpdRandomSample(-1, TriangularDistribution.with(0, 1)));
  }
}
