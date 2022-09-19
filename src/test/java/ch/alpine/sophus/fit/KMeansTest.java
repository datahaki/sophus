// code by jph
package ch.alpine.sophus.fit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Round;

class KMeansTest {
  @Test
  void testStandard() {
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 20, 2);
    KMeans kMeans = new KMeans( //
        biinvariant.distances(sequence), //
        RnBiinvariantMean.INSTANCE, //
        sequence);
    kMeans.setSeeds(List.of(0, 1));
    for (int i = 0; i < 10; ++i) {
      Tensor tensor = kMeans.iterate();
      System.out.println(tensor.map(Round._3));
    }
    Tensor partition = kMeans.partition();
    int sum = partition.stream().mapToInt(Tensor::length).sum();
    assertEquals(sum, sequence.length());
  }

  @Test
  void testSingle() {
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 20, 2);
    KMeans kMeans = new KMeans( //
        biinvariant.distances(sequence), //
        RnBiinvariantMean.INSTANCE, //
        sequence);
    kMeans.setSeeds(RandomVariate.of(NormalDistribution.standard(), 1, 2));
    for (int i = 0; i < 1; ++i) {
      Tensor tensor = kMeans.iterate();
      System.out.println(tensor.map(Round._3));
    }
    Tensor partition = kMeans.partition();
    int sum = partition.stream().mapToInt(Tensor::length).sum();
    assertEquals(sum, sequence.length());
  }
}
