// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.TrapezoidalDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnTransportTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    int d = 3;
    Distribution distribution = TrapezoidalDistribution.of(-3, -1, 1, 3);
    Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
    Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
    TensorUnaryOperator shift = Serialization.copy(HnTransport.INSTANCE.shift(p, q));
    Tensor vpq = new HnExponential(p).log(q);
    Tensor vqp = shift.apply(vpq).negate();
    Tensor exp = new HnExponential(q).exp(vqp);
    // System.out.println(vpq.length());
    // Tensor apply = new TSopqProject(d, 1).apply(vpq);
    // System.out.println(vpq);
    // System.out.println(apply);
    Chop._08.requireClose(p, exp);
  }
}
