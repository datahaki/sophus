// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.lie.sopq.TSopqProject;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.TrapezoidalDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnMetricBiinvariantTest extends TestCase {
  public void testCoordinateBiinvariant() throws ClassNotFoundException, IOException {
    Distribution distribution = TrapezoidalDistribution.of(-2, -1, 1, 2);
    int fails = 0;
    Biinvariant biinvariant = Serialization.copy(HnMetricBiinvariant.INSTANCE);
    for (int d = 1; d < 5; ++d) {
      int n = d + 1;
      try {
        ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
        Tensor sequence = //
            Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, n - 1)), d + 3);
        Tensor point = HnWeierstrassCoordinate.toPoint(Array.zeros(d));
        TensorUnaryOperator tensorUnaryOperator = //
            biinvariant.coordinate(HnManifold.INSTANCE, variogram, sequence);
        Tensor w1 = tensorUnaryOperator.apply(point);
        Tensor mean = HnBiinvariantMean.of(Chop._08).mean(sequence, w1);
        Chop._06.requireClose(mean, point);
        Tensor x = RandomVariate.of(NormalDistribution.standard(), n, n);
        x = new TSopqProject(d, 1).apply(x);
        Tensor sopq = MatrixExp.of(x);
        Tensor seq_l = Tensor.of(sequence.stream().map(sopq::dot));
        Tensor pnt_l = sopq.dot(point);
        Tensor w2 = biinvariant.coordinate(HnManifold.INSTANCE, variogram, seq_l).apply(pnt_l);
        Tensor m2 = HnBiinvariantMean.of(Chop._08).mean(seq_l, w2);
        Chop._06.requireClose(m2, pnt_l);
        Chop._06.requireClose(w1, w2);
      } catch (Exception exception) {
        System.err.println(d + " " + biinvariant);
        exception.printStackTrace();
        ++fails;
      }
    }
    assertTrue(fails <= 2);
  }

  public void testLagrangeBiinvariant() {
    Distribution distribution = TrapezoidalDistribution.of(-2, -1, 1, 2);
    int fails = 0;
    for (int d = 1; d < 5; ++d) {
      int n = d + 1;
      Biinvariant biinvariant = HnMetricBiinvariant.INSTANCE;
      try {
        ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
        Tensor sequence = //
            Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, n - 1)), d + 3);
        Tensor point = HnWeierstrassCoordinate.toPoint(Array.zeros(d));
        TensorUnaryOperator tensorUnaryOperator = //
            biinvariant.lagrainate(HnManifold.INSTANCE, variogram, sequence);
        Tensor w1 = tensorUnaryOperator.apply(point);
        Tensor mean = HnBiinvariantMean.of(Chop._08).mean(sequence, w1);
        Chop._06.requireClose(mean, point);
        Tensor x = RandomVariate.of(NormalDistribution.standard(), n, n);
        x = new TSopqProject(d, 1).apply(x);
        Tensor sopq = MatrixExp.of(x);
        Tensor seq_l = Tensor.of(sequence.stream().map(sopq::dot));
        Tensor pnt_l = sopq.dot(point);
        Tensor w2 = biinvariant.lagrainate(HnManifold.INSTANCE, variogram, seq_l).apply(pnt_l);
        Tensor m2 = HnBiinvariantMean.of(Chop._08).mean(seq_l, w2);
        Chop._06.requireClose(m2, pnt_l);
        Chop._06.requireClose(w1, w2);
      } catch (Exception exception) {
        System.err.println(d + " " + biinvariant);
        exception.printStackTrace();
        ++fails;
      }
    }
    assertTrue(fails <= 2);
  }
}
