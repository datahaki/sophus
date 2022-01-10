// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.dv.LeveragesDistanceVector;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;

/* package */ enum LeveragesDistancesDemo {
  ;
  public static void main(String[] args) {
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    BarycentricCoordinate w1 = HsCoordinates.wrap(vectorLogManifold, LeveragesDistanceVector.INSTANCE);
    Timing t1 = Timing.stopped();
    Timing t2 = Timing.stopped();
    for (int count = 0; count < 1000; ++count) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 100, 3);
      Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
      t1.start();
      w1.weights(sequence, point);
      t1.stop();
    }
    System.out.println(t1.seconds());
    System.out.println(t2.seconds());
  }
}
