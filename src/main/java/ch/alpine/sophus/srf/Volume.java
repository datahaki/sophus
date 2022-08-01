// code by jph
package ch.alpine.sophus.srf;

import java.util.Arrays;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.Det;

public enum Volume {
  ;
  public static Scalar of(SurfaceMesh surfaceMesh) {
    Scalar scalar = RealScalar.ZERO;
    for (int[] triangle : TriangulateMesh.faces(surfaceMesh)) {
      Tensor mat = Tensor.of(Arrays.stream(triangle).mapToObj(surfaceMesh.vrt::get));
      scalar = scalar.add(Det.of(mat));
    }
    return scalar.multiply(RationalScalar.of(1, 6));
  }
}
