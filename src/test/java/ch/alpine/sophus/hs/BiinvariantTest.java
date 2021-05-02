// code by jph
package ch.alpine.sophus.hs;

import java.io.IOException;
import java.util.Random;

import ch.alpine.sophus.hs.sn.SnPhongMean;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.itp.CrossAveraging;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class BiinvariantTest extends TestCase {
  public void testAbsolute() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Biinvariant biinvariant = MetricBiinvariant.EUCLIDEAN;
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    TensorUnaryOperator weightingInterface = Serialization.copy( //
        biinvariant.distances(RnManifold.INSTANCE, sequence));
    Tensor point = RandomVariate.of(distribution, 3);
    Tensor weights = weightingInterface.apply(point);
    weights.map(QuantityMagnitude.singleton("m"));
  }

  public void testBiinvariant() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    for (Biinvariant biinvariant : Biinvariants.values()) {
      Tensor sequence = RandomVariate.of(distribution, 10, 3);
      TensorUnaryOperator weightingInterface = //
          biinvariant.distances(RnManifold.INSTANCE, sequence);
      weightingInterface.apply(RandomVariate.of(distribution, 3));
    }
  }

  public void testWeighting() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    for (Biinvariant biinvariant : Biinvariants.values()) {
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
          biinvariant.weighting(RnManifold.INSTANCE, InversePowerVariogram.of(2), sequence));
      Tensor vector = tensorUnaryOperator.apply(RandomVariate.of(distribution, 3));
      Chop._08.requireClose(Total.ofVector(vector), RealScalar.ONE);
    }
  }

  public void testCoordinate() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    for (Biinvariant biinvariant : Biinvariants.values()) {
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
          biinvariant.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(2), sequence));
      Tensor vector = tensorUnaryOperator.apply(RandomVariate.of(distribution, 3));
      Chop._08.requireClose(Total.ofVector(vector), RealScalar.ONE);
    }
  }

  public void testSimplePD() throws ClassNotFoundException, IOException {
    Random random = new Random();
    for (Biinvariant biinvariant : Biinvariants.values()) {
      Distribution distribution = NormalDistribution.standard();
      int n = 4 + random.nextInt(4);
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
          biinvariant.weighting(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2), sequence));
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(4);
      Tensor values = RandomSample.of(randomSampleInterface, random, n);
      Tensor point = RandomVariate.of(distribution, random, 3);
      Tensor evaluate = CrossAveraging.of(tensorUnaryOperator, SnPhongMean.INSTANCE, values).apply(point);
      VectorQ.requireLength(evaluate, 5);
    }
  }
}