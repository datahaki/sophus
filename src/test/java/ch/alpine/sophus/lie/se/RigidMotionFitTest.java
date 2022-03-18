// code by jph
package ch.alpine.sophus.lie.se;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;

public class RigidMotionFitTest {
  @Test
  public void testExact() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    Tensor skew3 = Cross.skew3(Tensors.vector(-.1, .2, .3));
    Tensor rotation = OrthogonalMatrixQ.require(MatrixExp.of(skew3));
    for (int n = 5; n < 11; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor translation = RandomVariate.of(distribution, 3);
      Tensor target = Tensor.of(points.stream().map(p -> rotation.dot(p).add(translation)));
      RigidMotionFit rigidMotionFit = Serialization.copy(RigidMotionFit.of(points, target));
      Chop._08.requireClose(rotation, rigidMotionFit.rotation());
      Chop._08.requireClose(translation, rigidMotionFit.translation());
      Chop._08.requireClose(Det.of(rigidMotionFit.rotation()), RealScalar.ONE);
    }
  }

  @Test
  public void testWeights() throws ClassNotFoundException, IOException {
    Random random = new Random(3);
    Distribution distribution = NormalDistribution.standard();
    Tensor skew3 = Cross.skew3(Tensors.vector(-.1, .2, .3));
    Tensor rotation = OrthogonalMatrixQ.require(MatrixExp.of(skew3));
    for (int n = 5; n < 11; ++n) {
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.of(-0.1, 1), random, 10));
      Tensor points = RandomVariate.of(distribution, random, 10, 3);
      Tensor translation = RandomVariate.of(distribution, random, 3);
      Tensor target = Tensor.of(points.stream().map(p -> rotation.dot(p).add(translation)));
      RigidMotionFit rigidMotionFit = Serialization.copy(RigidMotionFit.of(points, target, weights));
      Chop._08.requireClose(rotation, rigidMotionFit.rotation());
      Chop._08.requireClose(translation, rigidMotionFit.translation());
      Chop._08.requireClose(Det.of(rigidMotionFit.rotation()), RealScalar.ONE);
      Chop._08.requireClose(target, Tensor.of(points.stream().map(rigidMotionFit)));
      RigidMotionFit rigidMotionInv = Serialization.copy(RigidMotionFit.of(target, points, weights));
      Tensor inv = Inverse.of(rotation);
      Chop._08.requireClose(inv, rigidMotionInv.rotation());
      Chop._08.requireClose(inv.dot(translation).negate(), rigidMotionInv.translation());
    }
  }

  @Test
  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    Distribution dist_weights = ExponentialDistribution.of(1);
    for (int d = 2; d < 6; ++d) {
      for (int n = d + 1; n < 11; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor target = RandomVariate.of(distribution, n, d);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(dist_weights, n));
        RigidMotionFit rigidMotionFit = RigidMotionFit.of(points, target, weights);
        Chop._08.requireClose(Det.of(rigidMotionFit.rotation()), RealScalar.ONE);
      }
    }
  }

  @Test
  public void testIdentityFail() {
    Distribution dist_weights = ExponentialDistribution.of(2);
    for (int d = 2; d < 6; ++d) {
      for (int n = d + 1; n < 11; ++n) {
        Distribution distribution = NormalDistribution.standard();
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(dist_weights, n));
        RigidMotionFit rigidMotionFit = RigidMotionFit.of(points, points, weights);
        Chop._07.requireClose(rigidMotionFit.rotation(), IdentityMatrix.of(d));
        Chop._07.requireClose(rigidMotionFit.translation(), Array.zeros(d));
      }
    }
  }

  @Test
  public void testSingle() {
    Tensor points = Tensors.of(Tensors.vector(1, 2, 3));
    RigidMotionFit rigidMotionFit = RigidMotionFit.of(points, points);
    Chop._10.requireClose(rigidMotionFit.rotation(), IdentityMatrix.of(3));
    Chop._10.requireAllZero(rigidMotionFit.translation());
  }

  @Test
  public void testFormatFail() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 6, 3);
    Tensor target = RandomVariate.of(distribution, 7, 3);
    AssertFail.of(() -> RigidMotionFit.of(points, target, Tensors.vector(1, 2, 3, 4, 5, 6)));
    AssertFail.of(() -> RigidMotionFit.of(points, target, Tensors.vector(1, 2, 3, 4, 5, 6, 7)));
  }

  @Test
  public void testZeroFail() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 6, 3);
    Tensor target = RandomVariate.of(distribution, 6, 3);
    AssertFail.of(() -> RigidMotionFit.of(points, target, Array.zeros(6)));
    AssertFail.of(() -> RigidMotionFit.of(points, target, Array.zeros(6).map(N.DOUBLE)));
  }

  @Test
  public void testNegativeOk() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 6, 3);
    Tensor target = RandomVariate.of(distribution, 6, 3);
    RigidMotionFit rigidMotionFit = //
        RigidMotionFit.of(points, target, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, -2, 3, 4, 5, 6)));
    assertTrue(rigidMotionFit.toString().startsWith("RigidMotionFit"));
  }
}
