// code by jph
package ch.alpine.sophus.itp;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.var.ExponentialVariogram;
import ch.alpine.sophus.math.var.PowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.sca.Chop;

class KrigingTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  // private static final Biinvariant[] BIINV = { Biinvariants.HARBOR };
  // private static final Biinvariant[] SYMME = { MetricBiinvariant.EUCLIDEAN, Biinvariants.HARBOR };

  @Test
  void testSimple2() {
    Random random = new Random();
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    PowerVariogram powerVariogram = PowerVariogram.of(1, 1.4);
    Map<Biinvariants, Biinvariant> map = Biinvariants.kriging(Se2CoveringGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      int n = 4 + random.nextInt(6);
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor values = RandomVariate.of(distributiox, n);
      Tensor covariance = DiagonalMatrix.with(ConstantArray.of(RealScalar.of(0.02), n));
      Sedarim tensorUnaryOperator1 = //
          biinvariant.var_dist(powerVariogram, points);
      Kriging kriging1 = Kriging.regression(tensorUnaryOperator1::sunder, points, values, covariance);
      Tensor est1 = kriging1.estimate(xya);
      Scalar var1 = kriging1.variance(xya);
      Tensor shift = RandomVariate.of(distribution, 3);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Sedarim tensorUnaryOperatorL = //
            biinvariant.var_dist(powerVariogram, all);
        Kriging krigingL = Kriging.regression(tensorUnaryOperatorL::sunder, all, values, covariance);
        Tensor one = tensorMapping.apply(xya);
        Chop._10.requireClose(est1, krigingL.estimate(one));
        Chop._10.requireClose(var1, krigingL.variance(one));
      }
    }
  }

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    Map<Biinvariants, Biinvariant> map = Biinvariants.kriging(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Sedarim weightingInterface = biinvariant.var_dist(variogram, sequence);
      Kriging kriging = Serialization.copy(Kriging.interpolation(weightingInterface::sunder, sequence, values));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = kriging.estimate(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, values.get(index));
      }
    }
  }

  @Test
  void testScalarValued() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    Map<Biinvariants, Biinvariant> map = Biinvariants.kriging(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Sedarim weightingInterface = biinvariant.var_dist(variogram, sequence);
      Kriging kriging = Serialization.copy(Kriging.interpolation(weightingInterface::sunder, sequence, values));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = kriging.estimate(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, values.get(index));
      }
    }
  }

  @Test
  void testBarycentric() throws ClassNotFoundException, IOException {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    int n = 5 + random.nextInt(5);
    Map<Biinvariants, Biinvariant> map = Biinvariants.kriging(RnGroup.INSTANCE);
    for (int d = 1; d < 4; ++d) {
      Tensor sequence = RandomVariate.of(distribution, n, d);
      for (Biinvariant biinvariant : map.values()) {
        Sedarim tensorUnaryOperator = Serialization.copy(biinvariant.var_dist(variogram, sequence));
        Kriging kriging = Serialization.copy(Kriging.barycentric(tensorUnaryOperator::sunder, sequence));
        for (int index = 0; index < sequence.length(); ++index) {
          Tensor tensor = kriging.estimate(sequence.get(index));
          Chop._08.requireClose(tensor, UnitVector.of(n, index));
          // ---
          Tensor point = RandomVariate.of(distribution, d);
          Tensor weights = kriging.estimate(point);
          AffineQ.require(weights, Chop._08);
        }
      }
    }
  }

  @Test
  void testQuantityAbsolute() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = new ExponentialVariogram(Quantity.of(3, "m"), RealScalar.of(2));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    Sedarim sedarim = Biinvariants.METRIC.of(RnGroup.INSTANCE).var_dist(variogram, sequence);
    Kriging kriging = Kriging.interpolation(sedarim::sunder, sequence, values);
    Scalar apply = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
    QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
  }

  @Test
  void testQuantityBiinvariant() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = ExponentialVariogram.of(3, 2);
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    // Map<Biinvariants, Biinvariant> map = Biinvariants.kriging();
    Biinvariant biinvariant = Biinvariants.HARBOR.of(RnGroup.INSTANCE);
    // for (Biinvariant biinvariant : map.values())
    {
      Sedarim weightingInterface = biinvariant.var_dist(variogram, sequence);
      Kriging kriging = Kriging.interpolation(weightingInterface::sunder, sequence, values);
      Scalar apply = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
    }
  }
}
