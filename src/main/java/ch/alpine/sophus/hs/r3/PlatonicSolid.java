package ch.alpine.sophus.hs.r3;

import java.util.List;

import ch.alpine.sophus.hs.r3.qh3.ConvexHull3D;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.ResourceData;

public enum PlatonicSolid {
  TETRAHEDRON(4, 3),
  CUBE(6, 4),
  OCTAHEDRON(8, 3),
  DODECAHEDRON(12, 5),
  ICOSAHEDRON(20, 3);

  private final int faceCount;
  private final int faceShape;
  private final Tensor vertices = ResourceData.of("/ch/alpine/sophus/" + name().toLowerCase() + ".csv");

  PlatonicSolid(int faceCount, int faceShape) {
    this.faceCount = faceCount;
    this.faceShape = faceShape;
  }

  public int faceCount() {
    return faceCount;
  }

  public int faceShape() {
    return faceShape;
  }

  public Tensor vertices() {
    return vertices.copy();
  }

  public List<int[]> faces() {
    return ConvexHull3D.of(vertices);
  }
}
