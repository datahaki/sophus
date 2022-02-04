// code by jph
package ch.alpine.sophus.lie.he;

import java.io.IOException;
import java.util.stream.IntStream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import junit.framework.TestCase;

public class HeDifferencesTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    int d = 3;
    Tensor x = RandomVariate.of(distribution, n, d);
    Tensor y = RandomVariate.of(distribution, n, d);
    Tensor z = RandomVariate.of(distribution, n);
    Tensor elements = Tensor.of(IntStream.range(0, n).mapToObj(i -> Tensors.of(x.get(i), y.get(i), z.Get(i))));
    Tensor differences = Serialization.copy(HeDifferences.INSTANCE).apply(elements);
    assertEquals(differences.length(), n - 1);
  }
}
