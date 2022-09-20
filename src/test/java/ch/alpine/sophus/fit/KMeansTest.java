// code by jph
package ch.alpine.sophus.fit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class KMeansTest {
  @RepeatedTest(4)
  void testR2Single(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 20, 2);
    KMeans kMeans = new KMeans( //
        biinvariant.distances(sequence), //
        RnBiinvariantMean.INSTANCE, //
        sequence);
    kMeans.setSeeds(RandomVariate.of(NormalDistribution.standard(), n, 2));
    for (int i = 0; i < 10; ++i) {
      kMeans.iterate();
      // System.out.println(tensor.map(Round._3));
    }
    Tensor partition = kMeans.partition();
    int sum = partition.stream().mapToInt(Tensor::length).sum();
    assertEquals(sum, sequence.length());
    assertTrue(0 < kMeans.seeds().length());
  }

  @Test
  void testR2Standard() {
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 20, 2);
    KMeans kMeans = new KMeans( //
        biinvariant.distances(sequence), //
        RnBiinvariantMean.INSTANCE, //
        sequence);
    kMeans.setSeeds(List.of(0, 1));
    // Do.of(null, 0)
    for (int i = 0; i < 10; ++i) {
      kMeans.iterate();
      // System.out.println(tensor.map(Round._3));
    }
    Tensor partition = kMeans.partition();
    int sum = partition.stream().mapToInt(Tensor::length).sum();
    assertEquals(sum, sequence.length());
  }

  @Test
  void testSe2Standard() {
    Biinvariant biinvariant = Biinvariants.LEVERAGES.ofSafe(Se2CoveringGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 40, 3);
    KMeans kMeans = new KMeans( //
        biinvariant.distances(sequence), //
        Se2CoveringBiinvariantMean.INSTANCE, //
        sequence);
    kMeans.setSeeds(List.of(0, 1));
    for (int i = 0; i < 10; ++i) {
      kMeans.iterate();
      // System.out.println(tensor.map(Round._3));
    }
    Tensor partition = kMeans.partition();
    int sum = partition.stream().mapToInt(Tensor::length).sum();
    assertEquals(sum, sequence.length());
  }
}
