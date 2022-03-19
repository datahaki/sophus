// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;

public class Se2CoveringBarycenterTest {
  @Test
  public void testZeros() {
    Tensor sequence = Tensors.fromString("{{2, 3, 4}, {1, 2, 3}, {-2, 1, 1}, {2, -1, -7}}");
    TensorUnaryOperator se2CoveringBarycenter = new Se2CoveringBarycenter(sequence);
    Tensor mean = Array.zeros(3);
    Tensor weights = se2CoveringBarycenter.apply(mean);
    AffineQ.require(weights, Chop._08);
    Tensor result = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tolerance.CHOP.requireClose(result, mean);
  }

  @Test
  public void testXY() {
    Tensor sequence = Tensors.fromString("{{2, 3, 4}, {1, 2, 3}, {-2, 1, 1}, {2, -1, -7}}");
    TensorUnaryOperator se2CoveringBarycenter = new Se2CoveringBarycenter(sequence);
    Tensor mean = Tensors.vector(0.3, 0.6, 0);
    Tensor weights = se2CoveringBarycenter.apply(mean);
    AffineQ.require(weights, Chop._08);
    Tensor result = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tolerance.CHOP.requireClose(result, mean);
  }

  @Test
  public void testXYA() {
    Tensor sequence = Tensors.fromString("{{2, 3, 4}, {1, 2, 3}, {-2, 1, 1}, {2, -1, -7}}");
    TensorUnaryOperator se2CoveringBarycenter = new Se2CoveringBarycenter(sequence);
    Tensor mean = Tensors.vector(0.3, 0.6, 0.9);
    Tensor weights = se2CoveringBarycenter.apply(mean);
    AffineQ.require(weights, Chop._08);
    Tensor result = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tolerance.CHOP.requireClose(result, mean);
  }

  @Test
  public void testRank() {
    Tensor sequence = Tensors.fromString("{{0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 1}}");
    Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(sequence);
    Tensor mean = Tensors.vector(0.75, 1, 0);
    Tensor weights = se2CoveringBarycenter.apply(mean);
    AffineQ.require(weights, Chop._08);
    Chop._10.requireClose(weights, Tensors.vector(-0.75, 0.75, 1, 0));
    Tensor result = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tolerance.CHOP.requireClose(result, mean);
    Scalar w = weights.Get(1);
    Se2CoveringBiinvariantMean.INSTANCE.mean(sequence.extract(0, 2), Tensors.of(RealScalar.ONE.subtract(w), w));
  }

  @Test
  public void testLengthFail() {
    assertThrows(Exception.class, () -> new Se2CoveringBarycenter(HilbertMatrix.of(5, 3)));
  }
}
