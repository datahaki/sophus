// code by jph
package ch.alpine.sophus.ref.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Chop;

class GeodesicCatmullClarkSubdivisionTest {
  @Test
  void testQuad() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = //
        new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor quad = CirclePoints.of(4);
    Tensor center = catmullClarkSubdivision.quad(quad.get(0), quad.get(1), quad.get(3), quad.get(2));
    Chop._10.requireAllZero(center);
  }

  @Test
  void testEdgeCorner() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor quad = Tensors.vector(1, 0, 0, 0, 0, 0);
    Tensor mid1 = catmullClarkSubdivision.quad(quad.get(0), quad.get(1), quad.get(2), quad.get(3));
    Tensor mid2 = catmullClarkSubdivision.quad(quad.get(2), quad.get(3), quad.get(4), quad.get(5));
    assertEquals(mid1, RationalScalar.of(1, 4));
    assertEquals(mid2, RationalScalar.of(0, 4));
    Tensor edge = catmullClarkSubdivision.quad(mid1, mid2, quad.get(2), quad.get(3));
    assertEquals(edge, RationalScalar.of(1, 16));
  }

  @Test
  void testEdgeCentral1() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor quad = Tensors.vector(0, 0, 1, 0, 0, 0);
    Tensor mid1 = catmullClarkSubdivision.quad(quad.get(0), quad.get(1), quad.get(2), quad.get(3));
    Tensor mid2 = catmullClarkSubdivision.quad(quad.get(2), quad.get(3), quad.get(4), quad.get(5));
    assertEquals(mid1, RationalScalar.of(1, 4));
    assertEquals(mid2, RationalScalar.of(1, 4));
    Tensor edge = catmullClarkSubdivision.quad(mid1, mid2, quad.get(2), quad.get(3));
    assertEquals(edge, RationalScalar.of(3, 8));
  }

  @Test
  void testEdgeCentral2() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor quad = Tensors.vector(0, 0, 0, 1, 0, 0);
    Tensor mid1 = catmullClarkSubdivision.quad(quad.get(0), quad.get(1), quad.get(2), quad.get(3));
    Tensor mid2 = catmullClarkSubdivision.quad(quad.get(2), quad.get(3), quad.get(4), quad.get(5));
    assertEquals(mid1, RationalScalar.of(1, 4));
    assertEquals(mid2, RationalScalar.of(1, 4));
    Tensor edge = catmullClarkSubdivision.quad(mid1, quad.get(2), quad.get(3), mid2);
    assertEquals(edge, RationalScalar.of(3, 8));
  }

  @Test
  void testCenterCorner() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor quad = Tensors.vector(1, 0, 0, 0, 0, 0, 0, 0, 0);
    Tensor mid00 = catmullClarkSubdivision.quad(quad.get(0), quad.get(1), quad.get(3), quad.get(4));
    Tensor mid01 = catmullClarkSubdivision.quad(quad.get(1), quad.get(2), quad.get(4), quad.get(5));
    Tensor mid10 = catmullClarkSubdivision.quad(quad.get(3), quad.get(4), quad.get(6), quad.get(7));
    Tensor mid11 = catmullClarkSubdivision.quad(quad.get(4), quad.get(5), quad.get(7), quad.get(8));
    assertEquals(mid00, RationalScalar.of(1, 4));
    assertEquals(mid01, RealScalar.of(0));
    assertEquals(mid10, RealScalar.of(0));
    assertEquals(mid11, RealScalar.of(0));
    Tensor edg0 = catmullClarkSubdivision.quad(mid00, quad.get(1), quad.get(4), mid01);
    assertEquals(edg0, RationalScalar.of(1, 16));
    Tensor edg1 = catmullClarkSubdivision.quad(mid00, quad.get(3), quad.get(4), mid10);
    assertEquals(edg1, RationalScalar.of(1, 16));
  }

  @Test
  void testRefine() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor grid = Tensors.fromString("{{0, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}}");
    Tensor refine = catmullClarkSubdivision.refine(grid);
    String string = "{{0, 0, 0, 0, 0, 0, 0}, {0, 1/4, 3/8, 1/4, 1/16, 0, 0}, {0, 3/8, 9/16, 3/8, 7/64, 1/16, 1/8}, {0, 1/4, 3/8, 1/4, 1/8, 1/4, 1/2}, {0, 0, 0, 0, 1/8, 1/2, 1}}";
    assertEquals(refine, Tensors.fromString(string));
  }

  @Test
  void testRefineX() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor grid = Tensors.fromString("{{0, 1}, {0, 1}, {0, 1}}");
    Tensor refine = Nest.of(catmullClarkSubdivision::refine, grid, 2);
    assertEquals(refine, Tensors.vector(i -> Subdivide.of(0, 1, 4), 9));
    ExactTensorQ.require(refine);
  }

  @Test
  void testRefineY() {
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(RnGroup.INSTANCE);
    Tensor grid = Tensors.fromString("{{0, 0}, {1, 1}, {2, 2}}");
    Tensor refine = Nest.of(catmullClarkSubdivision::refine, grid, 2);
    assertEquals(Transpose.of(refine), Tensors.vector(i -> Subdivide.of(0, 2, 8), 5));
    ExactTensorQ.require(refine);
  }
}
