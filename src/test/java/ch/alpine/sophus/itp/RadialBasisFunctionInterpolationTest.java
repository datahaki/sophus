// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.var.PowerVariogram;
import ch.alpine.sophus.math.var.VariogramFunctions;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class RadialBasisFunctionInterpolationTest {
  // public static final Biinvariant[] PDA = { Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR };
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Random random = new Random(3);
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, random, n, 3);
    Tensor values = RandomVariate.of(distribution, random, n, 2);
    VariogramFunctions[] vars = { VariogramFunctions.POWER, VariogramFunctions.INVERSE_POWER, VariogramFunctions.GAUSSIAN,
        VariogramFunctions.INVERSE_MULTIQUADRIC };
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values())
      for (VariogramFunctions variograms : vars) {
        TensorUnaryOperator weightingInterface = biinvariant.weighting(variograms.of(RealScalar.TWO), sequence);
        TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
            RadialBasisFunctionInterpolation.of(weightingInterface, sequence, values));
        int index = random.nextInt(sequence.length());
        Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
        Chop._08.requireClose(tensor, values.get(index));
      }
  }

  @Test
  void testNormalized() {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Random random = new Random(1);
    Tensor sequence = RandomVariate.of(distribution, random, n, 3);
    Tensor values = RandomVariate.of(distribution, random, n, 2);
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      TensorUnaryOperator weightingInterface = biinvariant.var_dist(PowerVariogram.of(1, 2), sequence);
      TensorUnaryOperator tensorUnaryOperator = //
          RadialBasisFunctionInterpolation.of(weightingInterface, sequence, values);
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
        Chop._06.requireClose(tensor, values.get(index));
      }
    }
  }

  @Test
  void testBarycentric() {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Map<Biinvariants, Biinvariant> map = Biinvariants.all(RnGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      TensorUnaryOperator weightingInterface = biinvariant.weighting(PowerVariogram.of(1, 2), sequence);
      TensorUnaryOperator tensorUnaryOperator = RadialBasisFunctionInterpolation.of(weightingInterface, sequence);
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
        Chop._05.requireClose(tensor, UnitVector.of(n, index));
        // ---
        Tensor point = RandomVariate.of(distribution, 3);
        Tensor weights = tensorUnaryOperator.apply(point);
        AffineQ.require(weights, Chop._08);
      }
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> RadialBasisFunctionInterpolation.of(null, Tensors.empty(), Tensors.empty()));
  }
}
