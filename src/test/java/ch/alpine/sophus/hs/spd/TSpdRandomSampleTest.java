// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.c.TriangularDistribution;

public class TSpdRandomSampleTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    TSpdRandomSample simRandomSample = new TSpdRandomSample(3, TriangularDistribution.with(0, 1));
    Serialization.copy(simRandomSample);
    SymmetricMatrixQ.require(RandomSample.of(simRandomSample));
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> new TSpdRandomSample(0, TriangularDistribution.with(0, 1)));
    assertThrows(Exception.class, () -> new TSpdRandomSample(-1, TriangularDistribution.with(0, 1)));
  }
}
