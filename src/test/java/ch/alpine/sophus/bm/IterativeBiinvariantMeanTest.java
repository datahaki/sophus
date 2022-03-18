// code by ob, jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2.Se2Manifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;

public class IterativeBiinvariantMeanTest {
  @Test
  public void testSE2() {
    Scalar TWO = RealScalar.of(2);
    Scalar ZERO = RealScalar.ZERO;
    Scalar rootOfTwo = Sqrt.of(TWO);
    Scalar rootOfTwoHalf = rootOfTwo.reciprocal();
    Scalar piFourth = Pi.HALF.divide(TWO);
    // ---
    Tensor p = Tensors.of(rootOfTwoHalf.negate(), rootOfTwoHalf, piFourth);
    Tensor q = Tensors.of(rootOfTwo, ZERO, ZERO);
    Tensor r = Tensors.of(rootOfTwoHalf.negate(), rootOfTwoHalf.negate(), piFourth.negate());
    // ---
    // Tensor sequence = Tensors.of(p, q, r);
    Tensor sequenceUnordered = Tensors.of(p, r, q);
    Tensor weights = Tensors.vector(1, 1, 1).divide(RealScalar.of(3));
    // ---
    double nom = Math.sqrt(2) - Math.PI / 4;
    double denom = 1 + Math.PI / 4 * (Math.sqrt(2) / (2 - Math.sqrt(2)));
    Tensor expected = Tensors.vector(nom / denom, 0, 0);
    IterativeBiinvariantMean bMI = IterativeBiinvariantMean.of(Se2Manifold.INSTANCE, Chop._12);
    Tensor actual = bMI.apply(sequenceUnordered, weights).orElseThrow();
    Tolerance.CHOP.requireClose(actual, expected);
  }
  // Tests form more groups however i think that e.g. HE1 could cause problems due to tensor of tensor structure.

  @Test
  public void testSome() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    int success = 0;
    for (int length = 2; length < 8; ++length) {
      Tensor sequence = RandomVariate.of(UniformDistribution.unit(), length, 3);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, length));
      Tensor actual = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
      IterativeBiinvariantMean biinvariantMeanImplicit = //
          Serialization.copy(IterativeBiinvariantMean.of(Se2CoveringManifold.INSTANCE, Chop._12));
      Optional<Tensor> result = biinvariantMeanImplicit.apply(sequence, weights);
      if (result.isPresent()) {
        Tolerance.CHOP.requireClose(actual, result.orElseThrow());
        ++success;
      }
    }
    assertTrue(2 < success);
  }
}
