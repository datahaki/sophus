// code by jph
package ch.alpine.sophus.hs.spd;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.sca.Chop;

class SpdMemberQTest {
  // private static final Biinvariants[] BIINVARIANTS = new Biinvariants[] { //
  // Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR, Biinvariants.CUPOLA };
  @Test
  void testSimple() {
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      SpdMemberQ.INSTANCE.require(RandomSample.of(rsi));
    }
  }

  @Test
  void testBiinvarianceSon() {
    Random random = new Random(4);
    int n = 2 + random.nextInt(3);
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(SpdManifold.INSTANCE);
    for (Entry<Biinvariants, Biinvariant> entry : map.entrySet())
      if (!entry.getKey().equals(Biinvariants.CUPOLA) || n < 4) {
        Biinvariant biinvariant = entry.getValue();
        int count = 1 + random.nextInt(3);
        int len = n * (n + 1) / 2 + count;
        RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
        Tensor sequence = RandomSample.of(rsi, len);
        Tensor mL = RandomSample.of(rsi);
        Tensor weights1 = biinvariant.coordinate(InversePowerVariogram.of(2), sequence).apply(mL);
        // ---
        Tensor g = RandomSample.of(SoRandomSample.of(n), random);
        Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
        Tensor mR = BasisTransform.ofForm(mL, g);
        Tensor weights2 = biinvariant.coordinate(InversePowerVariogram.of(2), sR).apply(mR);
        Chop._02.requireClose(weights1, weights2);
      }
  }

  @Test
  void testBiinvarianceGln() {
    Random random = new Random(4);
    int n = 2 + random.nextInt(2);
    Map<Biinvariants, Biinvariant> map = Biinvariants.magic4(SpdManifold.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      int count = 1 + random.nextInt(3);
      int len = n * (n + 1) / 2 + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, random, len);
      Tensor mL = RandomSample.of(rsi, random);
      Tensor weights1 = biinvariant.coordinate(InversePowerVariogram.of(2), sequence).apply(mL);
      // ---
      Tensor g = RandomVariate.of(NormalDistribution.standard(), random, n, n);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = BasisTransform.ofForm(mL, g);
      Tensor weights2 = biinvariant.coordinate(InversePowerVariogram.of(2), sR).apply(mR);
      Chop._02.requireClose(weights1, weights2);
    }
  }
}
