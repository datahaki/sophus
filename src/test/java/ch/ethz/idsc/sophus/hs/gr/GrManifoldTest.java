// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.alg.Binomial;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrManifoldTest extends TestCase {
  public void testBiinvariance() {
    int n = 4;
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    VectorLogManifold vectorLogManifold = GrManifold.INSTANCE;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      int d = Binomial.of(n, k).number().intValue();
      Tensor seq_o = RandomSample.of(randomSampleInterface, d + 2);
      Tensor pnt_o = RandomSample.of(randomSampleInterface);
      Tensor so = RandomSample.of(SoRandomSample.of(n));
      for (Biinvariant biinvariant : new Biinvariant[] { //
          MetricBiinvariant.RIEMANN, Biinvariants.LEVERAGES, Biinvariants.GARDEN }) {
        Tensor w_o = biinvariant.coordinate(vectorLogManifold, variogram, seq_o).apply(pnt_o);
        Tensor seq_l = Tensor.of(seq_o.stream().map(matrix -> BasisTransform.ofMatrix(matrix, so)));
        Tensor pnt_l = BasisTransform.ofMatrix(pnt_o, so);
        Tensor w_l = biinvariant.coordinate(vectorLogManifold, variogram, seq_l).apply(pnt_l);
        Chop._10.requireClose(w_o, w_l);
      }
    }
  }
}
