// code by jph
package ch.alpine.sophus.math;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.alg.RotateLeft;

public class SimplexD {
  public static Set<Tensor> of(List<int[]> faces) {
    SimplexD simplexD = new SimplexD();
    for (int[] face : faces)
      simplexD.feedFace(face);
    return simplexD.set;
  }

  private final Set<Tensor> set = new HashSet<>();

  public SimplexD() {
  }

  public void feedFace(int[] face) {
    Tensor v = Tensors.vectorInt(face);
    for (int i = 0; i < face.length; ++i) {
      Tensor edge = RotateLeft.of(v, i).extract(0, 2);
      if (!set.remove(edge))
        set.add(Reverse.of(edge));
    }
  }
}
