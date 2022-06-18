// code by jph
package ch.alpine.sophus.lie.so3;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.gl.GlGroupElement;
import ch.alpine.sophus.lie.so.SoGroupElement;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class So3GroupTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Serialization.copy(So3Group.INSTANCE);
  }

  @Test
  void testAdjoint() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      Tensor v = So3TestHelper.spawn_so3();
      Tensor tensor = LinearSolve.of(g, v).dot(g);
      AntisymmetricMatrixQ.require(tensor);
      // AntisymmetricMatrixQ.require(So3Exponential.INSTANCE.log(g));
      VectorQ.requireLength(So3Group.INSTANCE.vectorLog(g), 3);
    }
  }

  @Test
  void testLinearGroup() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3();
      SoGroupElement so3GroupElement = So3Group.INSTANCE.element(g);
      GlGroupElement linearGroupElement = GlGroup.INSTANCE.element(g);
      Tensor v = So3TestHelper.spawn_so3();
      Tolerance.CHOP.requireClose( //
          so3GroupElement.adjoint(v), //
          linearGroupElement.adjoint(v));
      Tolerance.CHOP.requireClose( //
          so3GroupElement.dL(v), //
          linearGroupElement.dL(v));
    }
  }

  @Test
  void testSimple2() {
    Tensor p = Rodrigues.vectorExp(Tensors.vector(1, 2, 3));
    Tensor q = Rodrigues.vectorExp(Tensors.vector(2, -1, 2));
    Tensor split = So3Group.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(OrthogonalMatrixQ.of(split, Chop._14));
  }

  @Test
  void testEndPoints() {
    Distribution distribution = NormalDistribution.of(0, .3);
    for (int index = 0; index < 10; ++index) {
      Tensor p = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Tensor q = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Chop._14.requireClose(p, So3Group.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._11.requireClose(q, So3Group.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }

  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(So3Group.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(So3Group.INSTANCE);

  @Test
  void testSimple3() {
    Tensor g1 = Rodrigues.vectorExp(Tensors.vector(0.2, 0.3, 0.4));
    Tensor g2 = Rodrigues.vectorExp(Tensors.vector(0.1, 0.0, 0.5));
    Tensor g3 = Rodrigues.vectorExp(Tensors.vector(0.3, 0.5, 0.2));
    Tensor g4 = Rodrigues.vectorExp(Tensors.vector(0.5, 0.2, 0.1));
    Tensor sequence = Tensors.of(g1, g2, g3, g4);
    Tensor mean = Rodrigues.vectorExp(Tensors.vector(0.4, 0.2, 0.3));
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      Tensor weights = barycentricCoordinate.weights(sequence, mean);
      Tensor defect = new MeanDefect(sequence, weights, So3Group.INSTANCE.exponential(mean)).tangent();
      Chop._10.requireAllZero(defect);
    }
  }

  @Test
  void testLinearReproduction() {
    Random random = new Random(4);
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int n = 4 + random.nextInt(2);
      {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, 3).stream().map(Rodrigues::vectorExp));
        Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, random, 3));
        Tensor weights1 = barycentricCoordinate.weights(sequence, mean);
        Tensor o2 = So3Group.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(sequence, weights1);
        Chop._08.requireClose(mean, o2);
        // ---
        LieGroupElement lieGroupElement = So3Group.INSTANCE.element(So3TestHelper.spawn_So3(random));
        Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
        Tensor weights2 = barycentricCoordinate.weights(seqlft, lieGroupElement.combine(mean));
        Chop._06.requireClose(weights1, weights2);
        // ---
        {
          TensorMapping tensorMapping = LIE_GROUP_OPS.inversion();
          Chop._06.requireClose(weights1, //
              barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean)));
        }
      }
    }
  }

  @Test
  void testLagrange() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.of(0.0, 0.1);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int n = 4 + random.nextInt(2);
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, 3).stream().map(Rodrigues::vectorExp));
      int index = 0;
      for (Tensor point : sequence) {
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        Chop._06.requireClose(weights, UnitVector.of(n, index));
        Tensor o2 = So3Group.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(sequence, weights);
        Chop._06.requireClose(point, o2);
        ++index;
      }
    }
  }

  @Test
  void testAffineLinearReproduction() {
    Random random = new Random(1);
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    BarycentricCoordinate AFFINE = AffineWrap.of(So3Group.INSTANCE);
    int n = 4 + random.nextInt(2);
    Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
    Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, 3));
    Tensor weights1 = AFFINE.weights(sequence, mean);
    Tensor o2 = So3Group.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(sequence, weights1);
    Chop._08.requireClose(mean, o2);
    // ---
    LieGroupElement lieGroupElement = So3Group.INSTANCE.element(So3TestHelper.spawn_So3());
    Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
    Tensor weights2 = AFFINE.weights(seqlft, lieGroupElement.combine(mean));
    Chop._10.requireClose(weights1, weights2);
    // ---
    TensorMapping tensorMapping = LIE_GROUP_OPS.inversion();
    Chop._10.requireClose(weights1, AFFINE.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean)));
  }

  @Test
  void testFailOrthogonal() {
    assertThrows(Exception.class, () -> So3Group.INSTANCE.log(So3TestHelper.spawn_so3()));
  }

  @Test
  void testDistance() {
    Tensor vector = Tensors.vector(0.2, 0.5, 0.3);
    Scalar distance = So3Group.INSTANCE.distance( //
        Rodrigues.vectorExp(Tensors.vector(0, 0, 0)), //
        Rodrigues.vectorExp(vector));
    Chop._15.requireClose(distance, Vector2Norm.of(vector));
  }

  @Test
  void test4x4Fail() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor p = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    Tensor q = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    OrthogonalMatrixQ.require(p);
    assertThrows(Exception.class, () -> So3Group.INSTANCE.distance(p, q));
  }
}
