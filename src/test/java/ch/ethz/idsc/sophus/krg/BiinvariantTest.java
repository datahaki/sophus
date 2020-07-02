// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.sn.SnPhongMean;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.itp.CrossAveraging;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.sophus.math.var.SphericalVariogram;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BiinvariantTest extends TestCase {
  public void testAbsolute() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Biinvariant biinvariant = Biinvariant.METRIC;
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    TensorUnaryOperator weightingInterface = Serialization.copy( //
        biinvariant.distances(RnManifold.INSTANCE, SphericalVariogram.of(Quantity.of(10, "m"), Quantity.of(2, "s")), sequence));
    Tensor point = RandomVariate.of(distribution, 3);
    Tensor weights = weightingInterface.apply(point);
    weights.map(QuantityMagnitude.singleton("s"));
  }

  public void testBiinvariant() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Biinvariant[] pda = { //
        Biinvariant.ANCHOR, //
        Biinvariant.HARBOR, //
        Biinvariant.NORM2 };
    for (Biinvariant biinvariant : pda) {
      Tensor sequence = RandomVariate.of(distribution, 10, 3);
      TensorUnaryOperator weightingInterface = //
          biinvariant.distances(RnManifold.INSTANCE, SphericalVariogram.of(RealScalar.of(10), Quantity.of(2, "s")), sequence);
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor weights = weightingInterface.apply(point);
      weights.map(QuantityMagnitude.singleton("s"));
    }
  }

  public void testWeighting() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    for (Biinvariant biinvariant : Biinvariant.values()) {
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
          biinvariant.weighting(RnManifold.INSTANCE, InversePowerVariogram.of(2), sequence));
      Tensor vector = tensorUnaryOperator.apply(RandomVariate.of(distribution, 3));
      Chop._08.requireClose(Total.ofVector(vector), RealScalar.ONE);
    }
  }

  public void testCoordinate() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    for (Biinvariant biinvariant : Biinvariant.values()) {
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
          biinvariant.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(2), sequence));
      Tensor vector = tensorUnaryOperator.apply(RandomVariate.of(distribution, 3));
      Chop._08.requireClose(Total.ofVector(vector), RealScalar.ONE);
    }
  }

  public void testSimplePD() throws ClassNotFoundException, IOException {
    for (Biinvariant biinvariant : Biinvariant.values()) {
      Distribution distribution = NormalDistribution.standard();
      for (int n = 10; n < 20; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, 3);
        TensorUnaryOperator shepardInterpolation = Serialization.copy( //
            biinvariant.weighting(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2), sequence));
        RandomSampleInterface randomSampleInterface = SnRandomSample.of(4);
        Tensor values = RandomSample.of(randomSampleInterface, n);
        Tensor point = RandomVariate.of(distribution, 3);
        Tensor evaluate = CrossAveraging.of(p -> shepardInterpolation.apply(p), SnPhongMean.INSTANCE, values).apply(point);
        VectorQ.requireLength(evaluate, 5);
      }
    }
  }
}
