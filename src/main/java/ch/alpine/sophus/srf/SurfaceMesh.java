// code by jph
package ch.alpine.sophus.srf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.io.Primitives;

public class SurfaceMesh implements Serializable {
  public final Tensor ind = Tensors.empty();
  public Tensor vrt = Tensors.empty();

  public int addVert(Tensor vector) {
    int index = vrt.length();
    vrt.append(vector);
    return index;
  }

  public void addFace(int... values) {
    ind.append(Tensors.vectorInt(values));
  }

  /** @return tensor of coordinates */
  public Tensor polygons() {
    return Tensor.of(ind.stream() //
        .map(Primitives::toIntArray) // deliberate: stream -> array -> stream
        .map(this::face)); //
  }

  private Tensor face(int[] indices) {
    return Tensor.of(Arrays.stream(indices).mapToObj(vrt::get));
  }

  /** @return vert to face index */
  public List<List<Integer>> vertToFace() {
    @SuppressWarnings("unused")
    List<List<Integer>> list = Stream.generate(() -> new ArrayList<Integer>()) //
        .limit(vrt.length()) //
        .collect(Collectors.toList());
    // ---
    int index = 0;
    for (Tensor face : ind) {
      for (int value : Primitives.toIntArray(face))
        list.get(value).add(index);
      ++index;
    }
    return list;
  }
}
