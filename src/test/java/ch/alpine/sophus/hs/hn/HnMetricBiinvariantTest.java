// code by jph
package ch.alpine.sophus.hs.hn;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.lie.sopq.TSopqProject;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TrapezoidalDistribution;
import ch.alpine.tensor.sca.Chop;

public class HnMetricBiinvariantTest {
  @Test
  public void testCoordinateBiinvariant() throws ClassNotFoundException, IOException {
    Random random = new Random(40);
    Distribution distribution = TrapezoidalDistribution.of(-2, -1, 1, 2);
    Biinvariant biinvariant = Serialization.copy(HnMetricBiinvariant.INSTANCE);
    for (int d = 1; d < 5; ++d) {
      int n = d + 1;
      ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
      Tensor sequence = //
          Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, n - 1)), d + 3);
      Tensor point = HnWeierstrassCoordinate.toPoint(Array.zeros(d));
      TensorUnaryOperator tensorUnaryOperator = //
          biinvariant.coordinate(HnManifold.INSTANCE, variogram, sequence);
      Tensor w1 = tensorUnaryOperator.apply(point);
      Tensor mean = HnBiinvariantMean.of(Chop._08).mean(sequence, w1);
      Chop._06.requireClose(mean, point);
      Tensor x = RandomVariate.of(NormalDistribution.standard(), random, n, n);
      x = new TSopqProject(d, 1).apply(x);
      Tensor sopq = MatrixExp.of(x);
      Tensor seq_l = Tensor.of(sequence.stream().map(sopq::dot));
      Tensor pnt_l = sopq.dot(point);
      Tensor w2 = biinvariant.coordinate(HnManifold.INSTANCE, variogram, seq_l).apply(pnt_l);
      Tensor m2 = HnBiinvariantMean.of(Chop._08).mean(seq_l, w2);
      Chop._06.requireClose(m2, pnt_l);
      Chop._06.requireClose(w1, w2);
    }
  }

  @Test
  public void testLagrangeBiinvariant() {
    Random random = new Random(40);
    Distribution distribution = TrapezoidalDistribution.of(-2, -1, 1, 2);
    for (int d = 1; d < 5; ++d) {
      int n = d + 1;
      Biinvariant biinvariant = HnMetricBiinvariant.INSTANCE;
      ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
      Tensor sequence = //
          Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, n - 1)), d + 3);
      Tensor point = HnWeierstrassCoordinate.toPoint(Array.zeros(d));
      TensorUnaryOperator tensorUnaryOperator = //
          biinvariant.lagrainate(HnManifold.INSTANCE, variogram, sequence);
      Tensor w1 = tensorUnaryOperator.apply(point);
      Tensor mean = HnBiinvariantMean.of(Chop._08).mean(sequence, w1);
      Chop._06.requireClose(mean, point);
      Tensor x = RandomVariate.of(NormalDistribution.standard(), random, n, n);
      x = new TSopqProject(d, 1).apply(x);
      Tensor sopq = MatrixExp.of(x);
      Tensor seq_l = Tensor.of(sequence.stream().map(sopq::dot));
      Tensor pnt_l = sopq.dot(point);
      Tensor w2 = biinvariant.lagrainate(HnManifold.INSTANCE, variogram, seq_l).apply(pnt_l);
      Tensor m2 = HnBiinvariantMean.of(Chop._08).mean(seq_l, w2);
      Chop._06.requireClose(m2, pnt_l);
      Chop._06.requireClose(w1, w2);
    }
  }
}
