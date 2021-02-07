// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.lie.son.SonRandomSample;
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
  private static final Biinvariants[] BIINVARIANTS = new Biinvariants[] { Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR };

  public void testSimple() {
    for (int n = 1; n < 10; ++n)
      SpdMemberQ.INSTANCE.require(TestHelper.generateSpd(n));
  }

  public void testBiinvarianceSon() {
    int fails = 0;
    for (int n = 2; n < 4; ++n) {
      for (Biinvariants biinvariants : BIINVARIANTS)
        for (int count = 0; count < 4; ++count)
          try {
            int fn = n;
            int len = n * n + count;
            Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
            Tensor mL = TestHelper.generateSpd(fn);
            Tensor weights1 = biinvariants.coordinate( //
                SpdManifold.INSTANCE, InversePowerVariogram.of(2), sequence).apply(mL);
            // ---
            Tensor g = RandomSample.of(SonRandomSample.of(n));
            Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
            Tensor mR = BasisTransform.ofForm(mL, g);
            Tensor weights2 = biinvariants.coordinate( //
                SpdManifold.INSTANCE, InversePowerVariogram.of(2), sR).apply(mR);
            Chop._06.requireClose(weights1, weights2);
          } catch (Exception exception) {
            System.out.println(getClass().getSimpleName() + " Son fail " + biinvariants);
            ++fails;
          }
    }
    assertTrue(fails < 5);
  }

  public void testBiinvarianceGln() {
    int fails = 0;
    for (int n = 2; n < 4; ++n) {
      for (Biinvariants biinvariants : BIINVARIANTS)
        for (int count = 0; count < 4; ++count)
          try {
            int fn = n;
            int len = n * n + count;
            Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
            Tensor mL = TestHelper.generateSpd(fn);
            Tensor weights1 = biinvariants.coordinate( //
                SpdManifold.INSTANCE, InversePowerVariogram.of(2), sequence).apply(mL);
            // ---
            Tensor g = RandomVariate.of(NormalDistribution.standard(), n, n);
            Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
            Tensor mR = BasisTransform.ofForm(mL, g);
            Tensor weights2 = biinvariants.coordinate( //
                SpdManifold.INSTANCE, InversePowerVariogram.of(2), sR).apply(mR);
            Chop._06.requireClose(weights1, weights2);
          } catch (Exception exception) {
            System.out.println(getClass().getSimpleName() + " Gln fail " + biinvariants);
            ++fails;
          }
    }
    assertTrue(fails < 5);
  }
}
