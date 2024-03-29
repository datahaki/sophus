// code by jph
package ch.alpine.sophus.srf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.alpine.sophus.math.IntDirectedEdge;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Integers;

public class SurfaceMesh implements Serializable {
  public Tensor vrt;
  private final List<int[]> faces;

  public SurfaceMesh() {
    vrt = Tensors.empty();
    faces = new ArrayList<>();
  }

  public SurfaceMesh(Tensor vertices, List<int[]> faces) {
    this.vrt = vertices;
    this.faces = faces;
  }

  /** @param tensor
   * @return index of tensor in list of vertices */
  public int addVert(Tensor tensor) {
    int index = vrt.length();
    vrt.append(tensor);
    return index;
  }

  // ---
  public void addFace(int... values) {
    long count = IntStream.of(values).distinct().count();
    Integers.requireEquals(values.length, (int) count);
    // if (values.length != count)
    // System.err.println(Tensors.vectorInt(values));
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
  public List<List<IntDirectedEdge>> vertToFace() {
    @SuppressWarnings("unused")
    List<List<IntDirectedEdge>> list = Stream.generate(() -> new ArrayList<IntDirectedEdge>()) //
        .limit(vrt.length()) //
        .collect(Collectors.toList());
    // ---
    for (int findex = 0; findex < faces.size(); ++findex) {
      int[] face = faces.get(findex);
      for (int vindex = 0; vindex < face.length; ++vindex)
        list.get(face[vindex]).add(new IntDirectedEdge(findex, vindex));
    }
    return list;
  }

  public Set<Integer> boundary() {
    Set<IntDirectedEdge> set = new HashSet<>();
    for (int[] face : faces) {
      for (int index = 0; index < face.length; ++index) {
        IntDirectedEdge intDirectedEdge = new IntDirectedEdge(face[index], face[(index + 1) % face.length]);
        if (set.contains(intDirectedEdge))
          set.remove(intDirectedEdge);
        else
          set.add(intDirectedEdge.reverse());
      }
    }
    return set.stream() //
        .flatMap(IntDirectedEdge::stream) //
        .collect(Collectors.toSet());
  }
}
