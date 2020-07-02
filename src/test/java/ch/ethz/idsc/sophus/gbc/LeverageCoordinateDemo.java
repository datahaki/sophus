// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Timing;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/* package */ enum LeverageCoordinateDemo {
  ;
  public static void main(String[] args) {
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    BarycentricCoordinate c1 = LeverageCoordinate.of(vectorLogManifold, variogram);
    BarycentricCoordinate c2 = LeverageCoordinate.slow(vectorLogManifold, variogram);
    Timing t1 = Timing.stopped();
    Timing t2 = Timing.stopped();
    for (int count = 0; count < 1000; ++count) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 100, 3);
      Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
      t2.start();
      c2.weights(sequence, point);
      t2.stop();
      t1.start();
      c1.weights(sequence, point);
      t1.stop();
    }
    System.out.println(t1.seconds());
    System.out.println(t2.seconds());
  }
}
