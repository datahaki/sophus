// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.Random;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.so.SoGroup;
import ch.alpine.sophus.math.TensorMapping;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3ManifoldTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(So3Manifold.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(SoGroup.INSTANCE);

  public void testSimple() {
    Tensor g1 = Rodrigues.vectorExp(Tensors.vector(0.2, 0.3, 0.4));
    Tensor g2 = Rodrigues.vectorExp(Tensors.vector(0.1, 0.0, 0.5));
    Tensor g3 = Rodrigues.vectorExp(Tensors.vector(0.3, 0.5, 0.2));
    Tensor g4 = Rodrigues.vectorExp(Tensors.vector(0.5, 0.2, 0.1));
    Tensor sequence = Tensors.of(g1, g2, g3, g4);
    Tensor mean = Rodrigues.vectorExp(Tensors.vector(0.4, 0.2, 0.3));
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      Tensor weights = barycentricCoordinate.weights(sequence, mean);
      Tensor defect = new MeanDefect(sequence, weights, So3Manifold.INSTANCE.exponential(mean)).tangent();
      Chop._10.requireAllZero(defect);
    }
  }

  public void testLinearReproduction() {
    Random random = new Random(4);
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int n = 4 + random.nextInt(2);
      {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, 3).stream().map(Rodrigues::vectorExp));
        Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, random, 3));
        Tensor weights1 = barycentricCoordinate.weights(sequence, mean);
        Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights1);
        Chop._08.requireClose(mean, o2);
        // ---
        LieGroupElement lieGroupElement = SoGroup.INSTANCE.element(So3TestHelper.spawn_So3(random));
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

  public void testLagrange() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.of(0.0, 0.1);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int n = 4 + random.nextInt(2);
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, 3).stream().map(Rodrigues::vectorExp));
      int index = 0;
      for (Tensor point : sequence) {
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        Chop._06.requireClose(weights, UnitVector.of(n, index));
        Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._06.requireClose(point, o2);
        ++index;
      }
    }
  }

  public void testAffineLinearReproduction() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    BarycentricCoordinate AFFINE = AffineWrap.of(So3Manifold.INSTANCE);
    int n = 4 + random.nextInt(2);
    Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
    Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, 3));
    Tensor weights1 = AFFINE.weights(sequence, mean);
    Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights1);
    Chop._08.requireClose(mean, o2);
    // ---
    LieGroupElement lieGroupElement = SoGroup.INSTANCE.element(So3TestHelper.spawn_So3());
    Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
    Tensor weights2 = AFFINE.weights(seqlft, lieGroupElement.combine(mean));
    Chop._10.requireClose(weights1, weights2);
    // ---
    TensorMapping tensorMapping = LIE_GROUP_OPS.inversion();
    Chop._10.requireClose(weights1, AFFINE.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean)));
  }
}
