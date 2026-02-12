// code by jph
package ch.alpine.sophus.math.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.EqualsReduce;
import ch.alpine.tensor.sca.Clips;

class DistanceMatrixTest {
  @Test
  void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Quantity.of(2, "A")));
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    Tensor tensor = DistanceMatrix.of(sequence, Vector2Norm::between);
    SquareMatrixQ.INSTANCE.require(tensor);
    assertEquals(EqualsReduce.zero(tensor), Quantity.of(0, "A"));
    SymmetricMatrixQ.INSTANCE.require(tensor);
  }

  @Test
  void testRankEuclidean() {
    RandomGenerator random = new Random(1);
    Distribution distribution = UniformDistribution.of(Clips.absolute(Quantity.of(2, "m")));
    Tensor sequence = RandomVariate.of(distribution, random, 10, 2);
    assertEquals(MatrixRank.of(DistanceMatrix.of(sequence, Vector2Norm::between)), 10);
    Tensor d = DistanceMatrix.of(sequence, Vector2NormSquared::between);
    assertEquals(MatrixRank.of(d), 4);
    Eigensystem eigensystem = Eigensystem.ofSymmetric(d);
    OrderedQ.require(Reverse.of(eigensystem.decreasing().values()));
  }
}
