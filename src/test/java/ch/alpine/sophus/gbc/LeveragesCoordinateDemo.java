// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/* package */ enum LeveragesCoordinateDemo {
  ;
  public static void main(String[] args) {
    VectorLogManifold vectorLogManifold = RnGroup.INSTANCE;
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    BarycentricCoordinate c1 = LeveragesCoordinate.of(vectorLogManifold, variogram);
    Timing t1 = Timing.stopped();
    Timing t2 = Timing.stopped();
    for (int count = 0; count < 1000; ++count) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 100, 3);
      Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
      t1.start();
      c1.weights(sequence, point);
      t1.stop();
    }
    System.out.println(t1.seconds());
    System.out.println(t2.seconds());
  }
}
