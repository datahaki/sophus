// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.IOException;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.var.PowerVariogram;
import ch.ethz.idsc.sophus.math.var.Variograms;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RadialBasisFunctionInterpolationTest extends TestCase {
  public static final Biinvariant[] PDA = { Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR };

  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    Variograms[] vars = { Variograms.POWER, Variograms.INVERSE_POWER, Variograms.GAUSSIAN, Variograms.INVERSE_MULTIQUADRIC };
    for (Biinvariant biinvariant : PDA)
      for (Variograms variograms : vars) {
        TensorUnaryOperator weightingInterface = biinvariant.weighting(RnManifold.INSTANCE, variograms.of(RealScalar.TWO), sequence);
        TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
            RadialBasisFunctionInterpolation.of(weightingInterface, sequence, values));
        for (int index = 0; index < sequence.length(); ++index) {
          Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
          Chop._08.requireClose(tensor, values.get(index));
        }
      }
  }

  public void testNormalized() {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Random random = new Random(1);
    Tensor sequence = RandomVariate.of(distribution, random, n, 3);
    Tensor values = RandomVariate.of(distribution, random, n, 2);
    for (Biinvariant biinvariant : PDA) {
      TensorUnaryOperator weightingInterface = biinvariant.var_dist(RnManifold.INSTANCE, PowerVariogram.of(1, 2), sequence);
      TensorUnaryOperator tensorUnaryOperator = //
          RadialBasisFunctionInterpolation.of(weightingInterface, sequence, values);
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
        Chop._06.requireClose(tensor, values.get(index));
      }
    }
  }

  public void testBarycentric() {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    for (Biinvariant biinvariant : PDA) {
      TensorUnaryOperator weightingInterface = biinvariant.weighting(RnManifold.INSTANCE, PowerVariogram.of(1, 2), sequence);
      TensorUnaryOperator tensorUnaryOperator = RadialBasisFunctionInterpolation.of(weightingInterface, sequence);
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
        Chop._08.requireClose(tensor, UnitVector.of(n, index));
        // ---
        Tensor point = RandomVariate.of(distribution, 3);
        Tensor weights = tensorUnaryOperator.apply(point);
        AffineQ.require(weights, Chop._08);
      }
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> RadialBasisFunctionInterpolation.of(null, Tensors.empty(), Tensors.empty()));
  }
}
