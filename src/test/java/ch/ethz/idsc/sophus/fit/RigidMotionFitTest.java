// code by jph
package ch.ethz.idsc.sophus.fit;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.re.Det;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.ExponentialDistribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.N;
import junit.framework.TestCase;

public class RigidMotionFitTest extends TestCase {
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

  public void testWeights() {
    Distribution distribution = NormalDistribution.standard();
    Tensor skew3 = Cross.skew3(Tensors.vector(-.1, .2, .3));
    Tensor rotation = OrthogonalMatrixQ.require(MatrixExp.of(skew3));
    int fails = 0;
    for (int n = 5; n < 11; ++n)
      try {
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.of(-0.1, 1), 10));
        Tensor points = RandomVariate.of(distribution, 10, 3);
        Tensor translation = RandomVariate.of(distribution, 3);
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
      } catch (Exception exception) {
        ++fails;
      }
    assertTrue(fails <= 2);
  }

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

  public void testSingle() {
    Tensor points = Tensors.of(Tensors.vector(1, 2, 3));
    RigidMotionFit rigidMotionFit = RigidMotionFit.of(points, points);
    Chop._10.requireClose(rigidMotionFit.rotation(), IdentityMatrix.of(3));
    Chop._10.requireAllZero(rigidMotionFit.translation());
  }

  public void testFormatFail() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 6, 3);
    Tensor target = RandomVariate.of(distribution, 7, 3);
    AssertFail.of(() -> RigidMotionFit.of(points, target, Tensors.vector(1, 2, 3, 4, 5, 6)));
    AssertFail.of(() -> RigidMotionFit.of(points, target, Tensors.vector(1, 2, 3, 4, 5, 6, 7)));
  }

  public void testZeroFail() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 6, 3);
    Tensor target = RandomVariate.of(distribution, 6, 3);
    AssertFail.of(() -> RigidMotionFit.of(points, target, Array.zeros(6)));
    AssertFail.of(() -> RigidMotionFit.of(points, target, Array.zeros(6).map(N.DOUBLE)));
  }

  public void testNegativeOk() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 6, 3);
    Tensor target = RandomVariate.of(distribution, 6, 3);
    RigidMotionFit rigidMotionFit = //
        RigidMotionFit.of(points, target, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, -2, 3, 4, 5, 6)));
    assertTrue(rigidMotionFit.toString().startsWith("RigidMotionFit"));
  }
}
