// code by jph
package ch.alpine.sophus.dv;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.hs.sn.SnPhongMean;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.itp.CrossAveraging;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class BiinvariantTest {
  @Test
  void testAbsolute() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Biinvariant biinvariant = new MetricBiinvariant(RnGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    Sedarim weightingInterface = Serialization.copy(biinvariant.distances(sequence));
    Tensor point = RandomVariate.of(distribution, 3);
    Tensor weights = weightingInterface.sunder(point);
    weights.map(QuantityMagnitude.singleton("m"));
  }

  @Test
  void testBiinvariant() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Tensor sequence = RandomVariate.of(distribution, 10, 3);
      Sedarim weightingInterface = biinvariant.distances(sequence);
      weightingInterface.sunder(RandomVariate.of(distribution, 3));
    }
  }

  @Test
  void testWeighting() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      Sedarim tensorUnaryOperator = Serialization.copy( //
          biinvariant.weighting(InversePowerVariogram.of(2), sequence));
      Tensor vector = tensorUnaryOperator.sunder(RandomVariate.of(distribution, 3));
      Chop._08.requireClose(Total.ofVector(vector), RealScalar.ONE);
    }
  }

  @Test
  void testCoordinate() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      Sedarim tensorUnaryOperator = Serialization.copy( //
          biinvariant.coordinate(InversePowerVariogram.of(2), sequence));
      Tensor vector = tensorUnaryOperator.sunder(RandomVariate.of(distribution, 3));
      Chop._08.requireClose(Total.ofVector(vector), RealScalar.ONE);
    }
  }

  @Test
  void testSimplePD() throws ClassNotFoundException, IOException {
    Random random = new Random();
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(Se2CoveringGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Distribution distribution = NormalDistribution.standard();
      int n = 4 + random.nextInt(4);
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      Sedarim tensorUnaryOperator = Serialization.copy( //
          biinvariant.weighting(InversePowerVariogram.of(2), sequence));
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(4);
      Tensor values = RandomSample.of(randomSampleInterface, random, n);
      Tensor point = RandomVariate.of(distribution, random, 3);
      // TODO SOPHUS SN PHONG !?
      Tensor evaluate = new CrossAveraging(tensorUnaryOperator::sunder, SnPhongMean.INSTANCE, values).apply(point);
      VectorQ.requireLength(evaluate, 5);
    }
  }
}
