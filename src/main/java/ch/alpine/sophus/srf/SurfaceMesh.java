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

public class SurfaceMesh implements Serializable {
  public Tensor vrt = Tensors.empty();
  private final List<int[]> faces = new ArrayList<>();

  /** @param tensor
   * @return index of tensor in list of vertices */
  public int addVert(Tensor tensor) {
    int index = vrt.length();
    vrt.append(tensor);
    return index;
  }

  // ---
  public void addFace(int... values) {
    faces.add(values);
  }

  public List<int[]> faces() {
    return faces;
  }

  public int[] face(int index) {
    return faces.get(index);
  }

  // ---
  /** @return tensor of coordinates */
  public Tensor polygons() {
    return Tensor.of(faces.stream().map(this::polygon_face));
  }

  public Tensor polygon_face(int... indices) {
    return Tensor.of(Arrays.stream(indices).mapToObj(vrt::get));
  }

  // ---
  /** @return vert to face index */
  public List<List<Integer>> vertToFace() {
    @SuppressWarnings("unused")
    List<List<Integer>> list = Stream.generate(() -> new ArrayList<Integer>()) //
        .limit(vrt.length()) //
        .collect(Collectors.toList());
    // ---
    int index = 0;
    for (int[] face : faces) {
      for (int value : face)
        list.get(value).add(index);
      ++index;
    }
    return list;
  }
}
