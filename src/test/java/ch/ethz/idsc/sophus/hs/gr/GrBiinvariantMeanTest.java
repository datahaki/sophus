// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.ExponentialDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrBiinvariantMeanTest extends TestCase {
  public void testBiinvariant() {
    Distribution distribution = ExponentialDistribution.of(1);
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(4, 2); // 4 dimensional
    Scalar maxDist = RealScalar.of(1.4);
    for (int count = 0; count < 10; ++count) {
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
      AssertFail.of(() -> GrBiinvariantMean.INSTANCE.mean(sequence, RandomVariate.of(distribution, n)));
      Tensor point = GrBiinvariantMean.INSTANCE.mean(sequence, weights);
      GrMemberQ.INSTANCE.require(point);
      GrMetric.INSTANCE.distance(p, point);
      {
        Tensor g = RandomSample.of(SoRandomSample.of(4));
        GrAction grAction = new GrAction(g);
        Tensor seq_l = Tensor.of(sequence.stream().map(grAction));
        Tensor pnt_l = GrBiinvariantMean.INSTANCE.mean(seq_l, weights);
        Chop._08.requireClose(grAction.apply(point), pnt_l);
      }
    }
  }

  public void testGeodesic() {
    HsGeodesic hsGeodesic = new HsGeodesic(GrManifold.INSTANCE);
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(4, 2); // 4 dimensional
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      ScalarTensorFunction scalarTensorFunction = hsGeodesic.curve(p, q);
      Tensor sequence = Subdivide.of(-1.1, 2.1, 6).map(scalarTensorFunction);
      for (Tensor point : sequence)
        GrMemberQ.INSTANCE.require(point);
    }
  }
}
