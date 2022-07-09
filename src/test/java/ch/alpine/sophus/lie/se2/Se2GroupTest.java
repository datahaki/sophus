// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.LogNormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class Se2GroupTest {
  @Test
  void testSimple() {
    Se2GroupElement se2GroupElement = Se2Group.INSTANCE.element(Tensors.vector(1, 2, 2 * Math.PI + 3));
    Tensor tensor = se2GroupElement.combine(Tensors.vector(0, 0, 0));
    assertEquals(tensor, Tensors.vector(1, 2, 3));
  }

  @Test
  void testSimple2() {
    Tensor split = Se2Group.INSTANCE.split(Tensors.vector(0, 0, 0), Tensors.vector(10, 0, 1), RealScalar.of(0.7));
    Chop._13.requireClose(split, Tensors.fromString("{7.071951896570488, -1.0688209919859546, 0.7}"));
  }

  @Test
  void testModPi() {
    Tensor split = Se2Group.INSTANCE.split(Tensors.vector(0, 0, 10), Tensors.vector(0, 0, 10), RealScalar.of(0.738));
    Chop._13.requireClose(split, Tensors.of(RealScalar.ZERO, RealScalar.ZERO, So2.MOD.apply(RealScalar.of(10))));
  }

  @Test
  void testBiinvariantMean() {
    Random random = new Random(1);
    Distribution distribution = UniformDistribution.of(-10, 8);
    Distribution wd = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomVariate.of(distribution, random, 3);
      Tensor q = RandomVariate.of(distribution, random, 3);
      Scalar w = RandomVariate.of(wd, random);
      Tensor mean = Se2BiinvariantMeans.FILTER.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
      Tensor splt = Se2Group.INSTANCE.split(p, q, w);
      splt.set(So2.MOD, 2);
      Chop._12.requireClose(mean, splt);
    }
  }

  @Test
  void testSimple3() {
    int n = 5 + new Random().nextInt(5);
    Tensor sequence = RandomSample.of(Se2RandomSample.of(LogNormalDistribution.standard()), n);
    Biinvariant biinvariant = Biinvariants.HARBOR.ofSafe(Se2Group.INSTANCE);
    Sedarim tuo = biinvariant.distances(sequence);
    Tensor matrix = Tensor.of(sequence.stream().map(tuo::sunder));
    assertEquals(Dimensions.of(matrix), Arrays.asList(n, n));
    assertTrue(SymmetricMatrixQ.of(matrix));
    // matrix entry i,j contains frobenius norm between
    // projection matrices at point i, and at point j
  }

  @Test
  void testUnits() {
    Tensor p = Tensors.fromString("{4.9[m], 4.9[m], 0.9}");
    Tensor q = Tensors.fromString("{5.0[m], 5.0[m], 1.0}");
    Tensor r = Tensors.fromString("{5.1[m], 5.1[m], 1.1}");
    Tensor s = Tensors.fromString("{4.8[m], 5.2[m], 1.3}");
    Se2GroupElement gp = Se2Group.INSTANCE.element(p);
    Se2GroupElement gq = Se2Group.INSTANCE.element(q);
    Se2GroupElement gr = Se2Group.INSTANCE.element(r);
    Se2GroupElement gs = Se2Group.INSTANCE.element(s);
    gp.combine(q);
    gp.combine(r);
    gp.combine(s);
    gq.combine(p);
    gq.combine(r);
    gq.combine(s);
    gr.combine(p);
    gr.combine(q);
    gr.combine(s);
    gs.combine(p);
    gs.combine(q);
    gs.combine(r);
  }
}
