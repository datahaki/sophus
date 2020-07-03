// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.var.ExponentialVariogram;
import ch.ethz.idsc.sophus.math.var.PowerVariogram;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class KrigingTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final Biinvariant[] BIINV = { Biinvariant.HARBOR };
  private static final Biinvariant[] SYMME = { Biinvariant.METRIC, Biinvariant.HARBOR };

  public void testSimple2() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    PowerVariogram powerVariogram = PowerVariogram.of(1, 1.4);
    for (Biinvariant biinvariant : BIINV)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor values = RandomVariate.of(distributiox, n);
        Tensor covariance = DiagonalMatrix.with(ConstantArray.of(RealScalar.of(0.02), n));
        TensorUnaryOperator tensorUnaryOperator1 = //
            biinvariant.var_dist(Se2CoveringManifold.INSTANCE, powerVariogram, points);
        Kriging kriging1 = Kriging.regression(tensorUnaryOperator1, points, values, covariance);
        Tensor est1 = kriging1.estimate(xya);
        Scalar var1 = kriging1.variance(xya);
        Tensor shift = RandomVariate.of(distribution, 3);
        { // invariant under left action
          Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
          TensorUnaryOperator tensorUnaryOperatorL = //
              biinvariant.var_dist(Se2CoveringManifold.INSTANCE, powerVariogram, seqlft);
          Kriging krigingL = Kriging.regression(tensorUnaryOperatorL, seqlft, values, covariance);
          Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
          Chop._10.requireClose(est1, krigingL.estimate(xyalft));
          Chop._10.requireClose(var1, krigingL.variance(xyalft));
        }
        { // invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
          TensorUnaryOperator tensorUnaryOperatorR = //
              biinvariant.var_dist(Se2CoveringManifold.INSTANCE, powerVariogram, seqrgt);
          Kriging krigingR = Kriging.regression(tensorUnaryOperatorR, seqrgt, values, covariance);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Chop._10.requireClose(est1, krigingR.estimate(xyargt));
          Chop._10.requireClose(var1, krigingR.variance(xyargt));
        }
        { // invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
          TensorUnaryOperator tensorUnaryOperatorI = //
              biinvariant.var_dist(Se2CoveringManifold.INSTANCE, powerVariogram, seqinv);
          Kriging krigingI = Kriging.regression(tensorUnaryOperatorI, seqinv, values, covariance);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Chop._10.requireClose(est1, krigingI.estimate(xyainv));
          Chop._10.requireClose(var1, krigingI.variance(xyainv));
        }
      }
  }

  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (Biinvariant biinvariant : SYMME) {
      TensorUnaryOperator weightingInterface = //
          biinvariant.var_dist(RnManifold.INSTANCE, variogram, sequence);
      Kriging kriging = Serialization.copy(Kriging.interpolation(weightingInterface, sequence, values));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = kriging.estimate(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, values.get(index));
      }
    }
  }

  public void testScalarValued() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (Biinvariant biinvariant : SYMME) {
      TensorUnaryOperator weightingInterface = //
          biinvariant.var_dist(RnManifold.INSTANCE, variogram, sequence);
      Kriging kriging = Serialization.copy(Kriging.interpolation(weightingInterface, sequence, values));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = kriging.estimate(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, values.get(index));
      }
    }
  }

  public void testBarycentric() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (int n = 5; n < 10; ++n)
      for (int d = 1; d < 4; ++d) {
        Tensor sequence = RandomVariate.of(distribution, n, d);
        for (Biinvariant biinvariant : SYMME) {
          TensorUnaryOperator tensorUnaryOperator = //
              Serialization.copy(biinvariant.var_dist(RnManifold.INSTANCE, variogram, sequence));
          Kriging kriging = Serialization.copy(Kriging.barycentric(tensorUnaryOperator, sequence));
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

  public void testQuantityAbsolute() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = ExponentialVariogram.of(Quantity.of(3, "m"), RealScalar.of(2));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    TensorUnaryOperator weightingInterface = //
        Biinvariant.METRIC.var_dist(RnManifold.INSTANCE, variogram, sequence);
    Kriging kriging = Kriging.interpolation(weightingInterface, sequence, values);
    Scalar apply = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
    QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
  }

  public void testQuantityBiinvariant() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = ExponentialVariogram.of(3, 2);
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    for (Biinvariant biinvariant : BIINV) {
      TensorUnaryOperator weightingInterface = //
          biinvariant.var_dist(RnManifold.INSTANCE, variogram, sequence);
      Kriging kriging = Kriging.interpolation(weightingInterface, sequence, values);
      Scalar apply = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
    }
  }
}
