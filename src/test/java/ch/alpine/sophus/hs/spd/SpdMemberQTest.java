// code by jph
package ch.alpine.sophus.hs.spd;

import java.util.Random;

import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdMemberQTest extends TestCase {
  private static final Biinvariants[] BIINVARIANTS = new Biinvariants[] { //
      Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR, Biinvariants.CUPOLA };

  public void testSimple() {
    for (int n = 1; n < 10; ++n)
      SpdMemberQ.INSTANCE.require(TestHelper.generateSpd(n));
  }

  public void testBiinvarianceSon() {
    Random random = new Random(4);
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
    Random random = new Random(4);
    int n = 2 + random.nextInt(2);
    for (Biinvariants biinvariants : BIINVARIANTS) {
      int count = 1 + random.nextInt(3);
      int fn = n;
      int len = n * (n + 1) / 2 + count;
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn, random), len);
      Tensor mL = TestHelper.generateSpd(fn, random);
      Tensor weights1 = biinvariants.coordinate( //
          SpdManifold.INSTANCE, InversePowerVariogram.of(2), sequence).apply(mL);
      // ---
      Tensor g = RandomVariate.of(NormalDistribution.standard(), random, n, n);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = BasisTransform.ofForm(mL, g);
      Tensor weights2 = biinvariants.coordinate( //
          SpdManifold.INSTANCE, InversePowerVariogram.of(2), sR).apply(mR);
      Chop._02.requireClose(weights1, weights2);
    }
  }
}
