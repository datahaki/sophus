// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.sca.Chop;

class GrBiinvariantMeanTest {
  private static final BiinvariantMean BIINVARIANT_MEAN = GrManifold.INSTANCE.biinvariantMean(Chop._10);

  @Test
  public void testBiinvariant() {
    Distribution distribution = ExponentialDistribution.of(1);
    RandomSampleInterface randomSampleInterface = new GrRandomSample(4, 2); // 4 dimensional
    Scalar maxDist = RealScalar.of(1.4);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor sequence = Tensors.of(p);
    for (int iter = 0; iter < 10; ++iter) {
      Tensor q = RandomSample.of(randomSampleInterface);
      Scalar distance = GrMetric.INSTANCE.distance(p, q);
      if (Scalars.lessThan(distance, maxDist))
        sequence.append(q);
    }
    int n = sequence.length();
    Tensor weights = NormalizeTotal.FUNCTION.apply(AveragingWeights.of(n).add(RandomVariate.of(distribution, n)));
    assertThrows(Exception.class, () -> BIINVARIANT_MEAN.mean(sequence, RandomVariate.of(distribution, n)));
    Tensor point = BIINVARIANT_MEAN.mean(sequence, weights);
    GrMemberQ.INSTANCE.require(point);
    GrMetric.INSTANCE.distance(p, point);
    {
      Tensor g = RandomSample.of(SoRandomSample.of(4));
      GrAction grAction = new GrAction(g);
      Tensor seq_l = Tensor.of(sequence.stream().map(grAction));
      Tensor pnt_l = BIINVARIANT_MEAN.mean(seq_l, weights);
      Chop._08.requireClose(grAction.apply(point), pnt_l);
    }
  }

  @RepeatedTest(10)
  public void testGeodesic() {
    GeodesicSpace hsGeodesic = GrManifold.INSTANCE;
    RandomSampleInterface randomSampleInterface = new GrRandomSample(4, 2); // 4 dimensional
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    ScalarTensorFunction scalarTensorFunction = hsGeodesic.curve(p, q);
    Tensor sequence = Subdivide.of(-1.1, 2.1, 6).map(scalarTensorFunction);
    for (Tensor point : sequence)
      GrMemberQ.INSTANCE.require(point);
  }
}
