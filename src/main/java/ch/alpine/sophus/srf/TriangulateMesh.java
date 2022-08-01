// code by jph
package ch.alpine.sophus.srf;

import java.util.ArrayList;
import java.util.List;

/** <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/TriangulateMesh.html">TriangulateMesh</a> */
public enum TriangulateMesh {
  ;
  public static SurfaceMesh of(SurfaceMesh surfaceMesh) {
    return new SurfaceMesh(surfaceMesh.vrt.copy(), faces(surfaceMesh.faces()));
  }

  public static List<int[]> faces(List<int[]> faces) {
    List<int[]> list = new ArrayList<>(faces.size());
    for (int[] face : faces) {
      if (3 < face.length) {
        int limit = face.length - 1;
        for (int i = 1; i < limit; ++i)
          list.add(new int[] { face[0], face[i], face[1 + i] });
      } else
        list.add(face.clone());
    }
    return list;
  }
}
