// code by jph
package ch.alpine.sophus.srf;

/** <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/TriangulateMesh.html">TriangulateMesh</a> */
public enum TriangulateMesh {
  ;
  public static SurfaceMesh of(SurfaceMesh surfaceMesh) {
    SurfaceMesh out = new SurfaceMesh();
    out.vrt = surfaceMesh.vrt.copy();
    for (int[] face : surfaceMesh.faces()) {
      if (3 < face.length) {
        int limit = face.length - 1;
        for (int i = 1; i < limit; ++i)
          out.addFace(new int[] { face[0], face[i], face[1 + i] });
      } else
        out.addFace(face.clone());
    }
    return out;
  }
}
