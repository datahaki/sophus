// code by jph
package ch.alpine.sophus.srf.io;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;

public enum PlyFormat {
  ;
  private static final String SEPARATOR = " ";

  public static SurfaceMesh parse(List<String> list) {
    int vn = 0;
    int fn = 0;
    Iterator<String> iterator = list.iterator();
    if (!iterator.next().equals("ply"))
      throw new IllegalStateException();
    while (iterator.hasNext()) {
      String line = iterator.next().trim();
      if (line.startsWith("element vertex "))
        vn = Integer.parseInt(line.substring(15));
      if (line.startsWith("element face "))
        fn = Integer.parseInt(line.substring(13));
      if (line.equals("end_header"))
        break;
    }
    SurfaceMesh surfaceMesh = new SurfaceMesh();
    for (int index = 0; index < vn; ++index)
      surfaceMesh.addVert(Tensor.of(Stream.of(iterator.next().split(SEPARATOR)) //
          .map(Scalars::fromString)));
    for (int index = 0; index < fn; ++index)
      surfaceMesh.addFace(Stream.of(iterator.next().split(SEPARATOR)) //
          .skip(1) //
          .mapToInt(Integer::parseInt) //
          .toArray());
    return surfaceMesh;
  }
}
