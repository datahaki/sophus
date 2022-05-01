// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;

class GrManifoldTest {
  @Test
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
    RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
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

  @Test
  public void testCommute() {
    int n = 5;
    int k = 2;
    RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    // System.out.println(Pretty.of(p.dot(q).map(Round._3)));
    // System.out.println(Pretty.of(q.dot(p).map(Round._3)));
    Tolerance.CHOP.requireClose(p.dot(q), Transpose.of(q.dot(p)));
    Tolerance.CHOP.requireClose(p, p.dot(p));
    Tolerance.CHOP.requireClose(q, q.dot(q));
  }

  @Test
  public void testMismatch() {
    int n = 5;
    Tensor p1 = RandomSample.of(new GrRandomSample(n, 1));
    Tensor p2 = RandomSample.of(new GrRandomSample(n, 2));
    Tensor q = ConstantArray.of(Pi.VALUE, n, n);
    assertThrows(Exception.class, () -> new GrExponential(p1).log(q));
    assertThrows(Exception.class, () -> new GrExponential(p2).log(q));
  }
}
