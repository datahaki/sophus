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
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrManifoldTest extends TestCase {
  public void testBiinvariance() {
    Biinvariant[] biinvariants = new Biinvariant[] { //
        MetricBiinvariant.VECTORIZE0, //
        Biinvariants.LEVERAGES, //
        Biinvariants.GARDEN };
    Random random = new Random();
    int n = 3 + random.nextInt(2);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    VectorLogManifold vectorLogManifold = GrManifold.INSTANCE;
    int k = 1 + random.nextInt(n - 1);
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
    int d = k * (n - k);
    Tensor seq_o = RandomSample.of(randomSampleInterface, random, d + 2);
    Tensor pnt_o = RandomSample.of(randomSampleInterface, random);
    for (Biinvariant biinvariant : biinvariants) {
      Tensor w_o = biinvariant.coordinate(vectorLogManifold, variogram, seq_o).apply(pnt_o);
      GrAction grAction = new GrAction(RandomSample.of(SoRandomSample.of(n), random));
      Tensor seq_l = Tensor.of(seq_o.stream().map(grAction));
      Tensor pnt_l = grAction.apply(pnt_o);
      Tensor w_l = biinvariant.coordinate(vectorLogManifold, variogram, seq_l).apply(pnt_l);
      Chop._06.requireClose(w_o, w_l);
    }
  }

  public void testCommute() {
    int n = 5;
    int k = 2;
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    // System.out.println(Pretty.of(p.dot(q).map(Round._3)));
    // System.out.println(Pretty.of(q.dot(p).map(Round._3)));
    Tolerance.CHOP.requireClose(p.dot(q), Transpose.of(q.dot(p)));
    Tolerance.CHOP.requireClose(p, p.dot(p));
    Tolerance.CHOP.requireClose(q, q.dot(q));
  }

  public void testMismatch() {
    int n = 5;
    Tensor p1 = RandomSample.of(GrRandomSample.of(n, 1));
    Tensor p2 = RandomSample.of(GrRandomSample.of(n, 2));
    Tensor q = ConstantArray.of(Pi.VALUE, n, n);
    AssertFail.of(() -> new GrExponential(p1).log(q));
    AssertFail.of(() -> new GrExponential(p2).log(q));
  }
}
