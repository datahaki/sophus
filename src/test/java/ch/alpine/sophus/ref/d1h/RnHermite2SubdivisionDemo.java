// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.IOException;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Put;

/* package */ enum RnHermite2SubdivisionDemo {
  ;
  public static void main(String[] args) throws IOException {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    TensorIteration tensorIteration = RnHermite2Subdivisions.manifold().string(RealScalar.ONE, control);
    for (int count = 1; count <= 6; ++count)
      tensorIteration.iterate();
    Tensor iterate = tensorIteration.iterate();
    Put.of(HomeDirectory.file("hermite2.file"), iterate);
  }
}
