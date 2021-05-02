// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.lie.sopq.TSopqProject;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.MatrixExp;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnBiinvariantMeanTest extends TestCase {
  public void testAffineFail() {
    BiinvariantMean biinvariantMean = HnBiinvariantMean.of(Chop._12);
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(0, 0));
    Tensor y = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 0));
    Tensor result = biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    Chop._10.requireClose(result, Tensors.vector(0.45508986056222733, 0, 1.09868411346781));
    AssertFail.of(() -> biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.6, 0.5)));
  }

  public void testBiinvariant() {
    Distribution distribution = NormalDistribution.standard();
    int fails = 0;
    for (int d = 2; d < 4; ++d) {
      int n = d + 1;
      for (Biinvariant biinvariant : new Biinvariant[] { //
          HnMetricBiinvariant.INSTANCE, //
          Biinvariants.LEVERAGES, //
          Biinvariants.GARDEN })
        try {
          ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
          Tensor sequence = //
              Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, n - 1)), d + 10);
          Tensor point = HnWeierstrassCoordinate.toPoint(Mean.of(sequence).extract(0, d));
          TensorUnaryOperator tensorUnaryOperator = //
              biinvariant.coordinate(HnManifold.INSTANCE, variogram, sequence);
          Tensor w1 = tensorUnaryOperator.apply(point);
          Tensor mean = HnBiinvariantMean.of(Chop._08).mean(sequence, w1);
          Chop._06.requireClose(mean, point);
          Tensor x = RandomVariate.of(NormalDistribution.standard(), n, n);
          x = new TSopqProject(d, 1).apply(x);
          HnAction hnAction = new HnAction(MatrixExp.of(x));
          Tensor seq_l = Tensor.of(sequence.stream().map(hnAction));
          Tensor pnt_l = hnAction.apply(point);
          Tensor w2 = biinvariant.coordinate(HnManifold.INSTANCE, variogram, seq_l).apply(pnt_l);
          Tensor m2 = HnBiinvariantMean.of(Chop._08).mean(seq_l, w2);
          Chop._06.requireClose(m2, pnt_l);
          Chop._06.requireClose(w1, w2);
        } catch (Exception exception) {
          System.err.println(d + " " + biinvariant);
          ++fails;
        }
    }
    assertTrue(fails <= 2);
  }

  public void testNullFail() {
    AssertFail.of(() -> HnBiinvariantMean.of(null));
  }
}
