package ch.alpine.sophus.hs.r3;

import ch.alpine.sophus.hs.r3.qh3.ConvexHull3D;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.ResourceData;

public enum PlatonicSolid {
  TETRAHEDRON(4, 3),
  CUBE(6, 4),
  OCTAHEDRON(8, 3),
  DODECAHEDRON(12, 5),
  ICOSAHEDRON(20, 3);

  private final int faces;
  private final int shape;
  private final Tensor vertices = ResourceData.of("/ch/alpine/sophus/" + name().toLowerCase() + ".csv");

  PlatonicSolid(int faces, int shape) {
    this.faces = faces;
    this.shape = shape;
  }
  
  public int n() {
    return faces;
  }
  public int shape() {
    return shape;
  }

  public Tensor vertices() {
    return vertices.copy();
  }

  public int[][] faces() {
    return ConvexHull3D.of(vertices);
  }
}
