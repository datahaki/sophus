// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.util.Random;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Binomial;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrManifoldTest extends TestCase {
  public void testBiinvariance() {
    Biinvariant[] biinvariants = new Biinvariant[] { //
        MetricBiinvariant.RIEMANN, //
        Biinvariants.LEVERAGES, //
        Biinvariants.GARDEN };
    Random random = new Random();
    int n = 4;
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    VectorLogManifold vectorLogManifold = GrManifold.INSTANCE;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      int d = Binomial.of(n, k).number().intValue();
      Tensor seq_o = RandomSample.of(randomSampleInterface, random, d + 2);
      Tensor pnt_o = RandomSample.of(randomSampleInterface, random);
      Biinvariant biinvariant = biinvariants[random.nextInt(biinvariants.length)];
      Tensor w_o = biinvariant.coordinate(vectorLogManifold, variogram, seq_o).apply(pnt_o);
      GrAction grAction = new GrAction(RandomSample.of(SoRandomSample.of(n), random));
      Tensor seq_l = Tensor.of(seq_o.stream().map(grAction));
      Tensor pnt_l = grAction.apply(pnt_o);
      Tensor w_l = biinvariant.coordinate(vectorLogManifold, variogram, seq_l).apply(pnt_l);
      Chop._10.requireClose(w_o, w_l);
    }
  }

  public void testMismatch() {
    int n = 5;
    Tensor p1 = RandomSample.of(GrRandomSample.of(n, 1));
    Tensor p2 = RandomSample.of(GrRandomSample.of(n, 2));
    Tensor q = RandomSample.of(GrRandomSample.of(n, 4));
    AssertFail.of(() -> new GrExponential(p1).log(q));
    AssertFail.of(() -> new GrExponential(p2).log(q));
  }
}
