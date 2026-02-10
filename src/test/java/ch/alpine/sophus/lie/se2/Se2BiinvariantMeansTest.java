// code by ob
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.pow.Sqrt;
import test.wrap.BiinvariantMeanQ;

class Se2BiinvariantMeansTest {
  // This test is from the paper:
  // Source: "Bi-invariant Means in Lie Groups. Application toLeft-invariant Polyaffine Transformations." p38
  @Test
  void testArsignyPennec() throws ClassNotFoundException, IOException {
    Scalar TWO = RealScalar.of(2);
    Scalar ZERO = RealScalar.ZERO;
    Scalar rootOfTwo = Sqrt.FUNCTION.apply(TWO);
    Scalar rootOfTwoHalf = rootOfTwo.reciprocal();
    Scalar piFourth = Pi.HALF.divide(TWO);
    // ---
    Tensor p = Tensors.of(rootOfTwoHalf.negate(), rootOfTwoHalf, piFourth);
    Tensor q = Tensors.of(rootOfTwo, ZERO, ZERO);
    Tensor r = Tensors.of(rootOfTwoHalf.negate(), rootOfTwoHalf.negate(), piFourth.negate());
    // ---
    Tensor sequence = Tensors.of(p, q, r);
    Tensor sequenceUnordered = Tensors.of(p, r, q);
    Tensor weights = Tensors.vector(1, 1, 1).divide(RealScalar.of(3));
    // ---
    double nom = Math.sqrt(2) - Math.PI / 4;
    double denom = 1 + Math.PI / 4 * (Math.sqrt(2) / (2 - Math.sqrt(2)));
    Tensor expected = Tensors.vector(nom / denom, 0, 0);
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      Tensor actual = Serialization.copy(biinvariantMean).mean(sequence, weights);
      Tensor tangent = MeanDefect.of(sequenceUnordered, weights, Se2Group.INSTANCE.exponential(actual)).tangent();
      Tolerance.CHOP.requireAllZero(tangent);
      Tensor actualUnordered = biinvariantMean.mean(sequenceUnordered, weights);
      // ---
      Chop._14.requireClose(expected, actual);
      Chop._14.requireClose(actualUnordered, actual);
    }
  }

  @Test
  void testTrivial() {
    Tensor p = Tensors.of(Tensors.vector(1, 9, -1));
    Tensor weights = Tensors.vector(1);
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      Tensor actual = biinvariantMean.mean(p, weights);
      Tensor tangent = MeanDefect.of(p, weights, Se2Group.INSTANCE.exponential(actual)).tangent();
      Tolerance.CHOP.requireAllZero(tangent);
      Chop._14.requireClose(p.get(0), actual);
    }
  }

  @Test
  void testTranslation() {
    Tensor sequence = Tensors.empty();
    Tensor p = Tensors.vector(1, 1, 0);
    for (int index = 0; index < 7; ++index)
      sequence.append(p.multiply(RealScalar.of(index)));
    Tensor weights = Tensors.vector(0.05, 0.1, 0.2, 0.3, 0.2, 0.1, 0.05);
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      Tensor actual = biinvariantMean.mean(sequence, weights);
      Chop._14.requireClose(Tensors.vector(3, 3, 0), actual);
      Tensor tangent = MeanDefect.of(sequence, weights, Se2Group.INSTANCE.exponential(actual)).tangent();
      Tolerance.CHOP.requireAllZero(tangent);
    }
  }

  @Test
  void testRotation() {
    Tensor sequence = Tensors.empty();
    Tensor p = Tensors.vector(0, 0, 0.2);
    for (int index = 0; index < 7; ++index)
      sequence.append(p.multiply(RealScalar.of(index)));
    Tensor weights = Tensors.vector(0.05, 0.1, 0.2, 0.3, 0.2, 0.1, 0.05);
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      Tensor actual = biinvariantMean.mean(sequence, weights);
      Chop._14.requireClose(Tensors.vector(0, 0, 0.6), actual);
      Tensor tangent = MeanDefect.of(sequence, weights, Se2Group.INSTANCE.exponential(actual)).tangent();
      Tolerance.CHOP.requireAllZero(tangent);
    }
  }

  @Test
  void testOrderInvariance() {
    Tensor p = Tensors.vector(4.9, 4.9, 0.9);
    Tensor q = Tensors.vector(5.0, 5.0, 1.0);
    Tensor r = Tensors.vector(5.1, 5.1, 1.1);
    Tensor sequence1 = Tensors.of(q, r, p);
    Tensor sequence2 = Tensors.of(r, p, q);
    Tensor sequence3 = Tensors.of(p, q, r);
    Tensor weights = Tensors.vector(1, 1, 1).divide(RealScalar.of(3));
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      Tensor actual1 = biinvariantMean.mean(sequence1, weights);
      Tensor actual2 = biinvariantMean.mean(sequence2, weights);
      Tensor actual3 = biinvariantMean.mean(sequence3, weights);
      Chop._14.requireClose(actual1, actual2);
      Chop._14.requireClose(actual1, actual3);
    }
  }

  @Test
  void testOrderInvariance2() {
    Tensor p = Tensors.vector(4.9, 4.9, 0.9);
    Tensor q = Tensors.vector(5.0, 5.0, 1.0);
    Tensor r = Tensors.vector(5.1, 5.1, 1.1);
    Tensor s = Tensors.vector(4.8, 5.2, 1.3);
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor weights = Tensors.vector(3, 2, 1, 4).divide(RealScalar.of(10));
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values())
      // Chop._12.requireClose(solution, Tensors.vector(4.911144632104387, 5.064995814659804, 1.1));
      new BiinvariantMeanQ(biinvariantMean, Chop._12).check(sequence, weights);
  }

  @Test
  void testOrderInvariance3() {
    Tensor p = Tensors.fromString("{4.9[m], 4.9[m], 0.9}");
    Tensor q = Tensors.fromString("{5.0[m], 5.0[m], 1.0}");
    Tensor r = Tensors.fromString("{5.1[m], 5.1[m], 1.1}");
    Tensor s = Tensors.fromString("{4.8[m], 5.2[m], 1.3}");
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor weights = Tensors.vector(3, 2, 1, 4).divide(RealScalar.of(10));
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      // System.out.println(biinvariantMean);
      Tensor solution = biinvariantMean.mean(sequence, weights);
      boolean close1 = Chop._12.isClose(solution, Tensors.fromString("{4.911144632104387[m], 5.064995814659804[m], 1.1}"));
      boolean close2 = Chop._12.isClose(solution, Tensors.fromString("{4.911658712738642[m], 5.064497410160735[m], 1.0998987355880372}"));
      assertTrue(close1 || close2);
      new BiinvariantMeanQ(biinvariantMean, Chop._12).check(sequence, weights);
    }
  }

  @Test
  void testOrderInvarianceNegative() {
    Tensor p = Tensors.vector(14.9, -4.9, 10.9);
    Tensor q = Tensors.vector(15.0, -5.0, 11.0);
    Tensor r = Tensors.vector(15.1, -5.1, 11.1);
    Tensor s = Tensors.vector(14.8, -5.2, 11.3);
    Tensor sequence = Tensors.of(p, q, r, s);
    Tensor mask = Tensors.vector(3, 2, -1, 4);
    Tensor weights = mask.divide(Total.ofVector(mask));
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values()) {
      Tensor solution = biinvariantMean.mean(sequence, weights);
      Chop._01.requireClose(solution, Tensors.vector(14.83619642851975, -5.043678108261259, -1.466370614359171));
      new BiinvariantMeanQ(biinvariantMean, Chop._12).check(sequence, weights);
    }
  }

  @Test
  void testPermutations() {
    RandomSampleInterface randomSampleInterface = Se2RandomSample.of(ExponentialDistribution.standard());
    for (int length = 1; length < 6; ++length) {
      // here, we hope that no antipodal points are generated
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), length));
      // weights = weights.divide(Total.ofVector(weights));
      new BiinvariantMeanQ(Se2BiinvariantMeans.GLOBAL, Chop._12).check(sequence, weights);
    }
  }

  @Test
  void testGlobalHomogeneous() {
    RandomSampleInterface randomSampleInterface = Se2RandomSample.of(ExponentialDistribution.standard());
    for (int length = 1; length < 6; ++length) {
      // here, we hope that no antipodal points are generated
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      Tensor weights = RandomVariate.of(UniformDistribution.unit(), length);
      Tensor sol1 = Se2BiinvariantMeans.GLOBAL.mean(sequence, weights);
      Scalar scaling = RandomVariate.of(UniformDistribution.of(1, 2));
      Tensor sol2 = Se2BiinvariantMeans.GLOBAL.mean(sequence, weights.multiply(scaling));
      Chop._12.requireClose(sol1, sol2);
    }
  }

  @Test
  void testPermutationsPiHalf() {
    for (int length = 1; length < 6; ++length) {
      Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.HALF));
      // here, we hope that no antipodal points are generated
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor weights = RandomVariate.of(UniformDistribution.unit(), length);
      weights = weights.divide(Total.ofVector(weights));
      for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values())
        new BiinvariantMeanQ(biinvariantMean, Tolerance.CHOP).check(sequence, weights);
    }
  }

  @Test
  void testCombined() {
    Tensor sequence = Tensors.empty();
    Tensor p = Tensors.vector(1, 1, 0.1);
    for (int index = 0; index < 7; ++index)
      sequence.append(p.multiply(RealScalar.of(index)));
    Tensor weights = Tensors.vector(0.05, 0.1, 0.2, 0.3, 0.2, 0.1, 0.05);
    Tensor actual = Se2BiinvariantMeans.GLOBAL.mean(sequence, weights);
    Tensor expected = Tensors.fromString("{3.105184243650884, 2.8948157563491153, 0.3}");
    Chop._14.requireClose(expected, actual);
  }

  @Test
  void testEmpty() {
    for (BiinvariantMean biinvariantMean : Se2BiinvariantMeans.values())
      assertThrows(Exception.class, () -> biinvariantMean.mean(Tensors.empty(), Tensors.empty()));
  }
}
