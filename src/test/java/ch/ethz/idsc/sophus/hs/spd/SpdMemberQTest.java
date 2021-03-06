// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.util.Random;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdMemberQTest extends TestCase {
  private static final Biinvariants[] BIINVARIANTS = new Biinvariants[] { //
      Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR, Biinvariants.CUPOLA };

  public void testSimple() {
    for (int n = 1; n < 10; ++n)
      SpdMemberQ.INSTANCE.require(TestHelper.generateSpd(n));
  }

  public void testBiinvarianceSon() {
    Random random = new Random();
    int n = 2 + random.nextInt(3);
    for (Biinvariants biinvariants : BIINVARIANTS)
      if (!biinvariants.equals(Biinvariants.CUPOLA) || n < 4) {
        int count = 1 + random.nextInt(3);
        int fn = n;
        int len = n * (n + 1) / 2 + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor mL = TestHelper.generateSpd(fn);
        Tensor weights1 = biinvariants.coordinate( //
            SpdManifold.INSTANCE, InversePowerVariogram.of(2), sequence).apply(mL);
        // ---
        Tensor g = RandomSample.of(SoRandomSample.of(n), random);
        Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
        Tensor mR = BasisTransform.ofForm(mL, g);
        Tensor weights2 = biinvariants.coordinate( //
            SpdManifold.INSTANCE, InversePowerVariogram.of(2), sR).apply(mR);
        Chop._02.requireClose(weights1, weights2);
      }
  }

  public void testBiinvarianceGln() {
    int fails = 0;
    Random random = new Random();
    int n = 2 + random.nextInt(2);
    for (Biinvariants biinvariants : BIINVARIANTS)
      try {
        int count = 1 + random.nextInt(3);
        int fn = n;
        int len = n * (n + 1) / 2 + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor mL = TestHelper.generateSpd(fn);
        Tensor weights1 = biinvariants.coordinate( //
            SpdManifold.INSTANCE, InversePowerVariogram.of(2), sequence).apply(mL);
        // ---
        Tensor g = RandomVariate.of(NormalDistribution.standard(), random, n, n);
        Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
        Tensor mR = BasisTransform.ofForm(mL, g);
        Tensor weights2 = biinvariants.coordinate( //
            SpdManifold.INSTANCE, InversePowerVariogram.of(2), sR).apply(mR);
        Chop._02.requireClose(weights1, weights2);
      } catch (Exception exception) {
        exception.printStackTrace();
        ++fails;
      }
    assertTrue(fails <= 2);
  }
}
