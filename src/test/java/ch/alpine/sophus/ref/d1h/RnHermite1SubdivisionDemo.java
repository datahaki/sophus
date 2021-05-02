// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.IOException;

import ch.alpine.sophus.math.Do;
import ch.alpine.sophus.math.TensorIteration;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Put;

/* package */ enum RnHermite1SubdivisionDemo {
  ;
  public static void main(String[] args) throws IOException {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    TensorIteration tensorIteration = RnHermite1Subdivisions.instance().string(RealScalar.ONE, control);
    Tensor iterate = Do.of(tensorIteration::iterate, 6);
    Put.of(HomeDirectory.file("merrien.file"), iterate);
  }
}
