// code by jph
package ch.alpine.sophus.srf.io;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;

public enum Ply2Format {
  ;
  public static SurfaceMesh parse(Stream<String> stream) {
    List<String> list = stream.toList();
    int vn = Integer.parseInt(list.get(0));
    int fn = Integer.parseInt(list.get(1));
    SurfaceMesh surfaceMesh = new SurfaceMesh();
    for (int count = 0; count < vn; ++count) {
      String line = list.get(2 + count);
      surfaceMesh.addVert(Tensor.of(Arrays.stream(line.split(" ")).map(Scalars::fromString)));
    }
    int ofs = 2 + vn;
    for (int count = 0; count < fn; ++count) {
      String line = list.get(ofs + count);
      surfaceMesh.addFace(Arrays.stream(line.split(" ")) //
          .skip(1) //
          .mapToInt(Integer::parseInt) //
          .toArray());
    }
    return surfaceMesh;
  }
}
