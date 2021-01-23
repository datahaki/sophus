// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.gbc.AffineWrap;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.son.SonGroup;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3ManifoldTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(So3Manifold.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(SonGroup.INSTANCE);

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
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int fails = 0;
      for (int n = 4; n < 10; ++n)
        try {
          Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
          Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, 3));
          Tensor weights1 = barycentricCoordinate.weights(sequence, mean);
          Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights1);
          Chop._08.requireClose(mean, o2);
          // ---
          LieGroupElement lieGroupElement = SonGroup.INSTANCE.element(So3TestHelper.spawn_So3());
          Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
          Tensor weights2 = barycentricCoordinate.weights(seqlft, lieGroupElement.combine(mean));
          Chop._06.requireClose(weights1, weights2);
          // ---
          {
            TensorMapping tensorMapping = LIE_GROUP_OPS.inversion();
            Chop._06.requireClose(weights1, //
                barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean)));
          }
        } catch (Exception exception) {
          ++fails;
        }
      assertTrue(fails < 2);
    }
  }

  public void testLagrange() {
    Distribution distribution = NormalDistribution.of(0.0, 0.1);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
        int index = 0;
        for (Tensor mean : sequence) {
          Tensor weights = barycentricCoordinate.weights(sequence, mean);
          Chop._06.requireClose(weights, UnitVector.of(n, index));
          Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._06.requireClose(mean, o2);
          ++index;
        }
      }
  }

  public void testAffineLinearReproduction() {
    int fail = 0;
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    BarycentricCoordinate AFFINE = AffineWrap.of(So3Manifold.INSTANCE);
    for (int n = 4; n < 10; ++n)
      try {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(Rodrigues::vectorExp));
        Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, 3));
        Tensor weights1 = AFFINE.weights(sequence, mean);
        Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights1);
        Chop._08.requireClose(mean, o2);
        // ---
        LieGroupElement lieGroupElement = SonGroup.INSTANCE.element(So3TestHelper.spawn_So3());
        Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
        Tensor weights2 = AFFINE.weights(seqlft, lieGroupElement.combine(mean));
        Chop._10.requireClose(weights1, weights2);
        // ---
        TensorMapping tensorMapping = LIE_GROUP_OPS.inversion();
        Chop._10.requireClose(weights1, AFFINE.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean)));
      } catch (Exception exception) {
        ++fail;
      }
    assertTrue(fail < 3);
  }
  // public void testSpanFail() { // FIXME why does this not always fail
  // Distribution distribution = NormalDistribution.of(0.0, 0.3);
  // Distribution d2 = NormalDistribution.of(0.0, 0.1);
  // for (BarycentricCoordinate projectedCoordinate : BARYCENTRIC_COORDINATES)
  // for (int n = 1; n < 5; ++n) {
  // System.out.println("n=" + n);
  // Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream() //
  // .map(Rodrigues::vectorExp));
  // Tensor mean = Rodrigues.vectorExp(RandomVariate.of(d2, 3));
  // try {
  // Tensor weights = projectedCoordinate.weights(sequence, mean);
  // System.out.println(weights);
  // // fail();
  // } catch (Exception exception) {
  // System.err.println("fail");
  // }
  // }
  // }
}
