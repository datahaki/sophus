// code by jph
package ch.alpine.sophus.srf.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.ReadLine;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.red.Max;

class WavefrontFormatTest {
  @Test
  void testBlender0() throws IOException {
    try (InputStream inputStream = getClass().getResource("/ch/alpine/sophus/obj/blender0.obj").openStream()) {
      Wavefront wavefront = WavefrontFormat.parse(ReadLine.of(inputStream));
      assertEquals(wavefront.objects().size(), 2);
      assertEquals(wavefront.objects().get(0).name(), "Cylinder");
      assertEquals(wavefront.objects().get(1).name(), "Cube");
      assertEquals(Dimensions.of(wavefront.vertices()), Arrays.asList(72, 3));
      assertTrue(MatrixQ.of(wavefront.normals()));
      assertEquals(Dimensions.of(wavefront.normals()), Arrays.asList(40, 3));
      List<WavefrontObject> objects = wavefront.objects();
      assertEquals(objects.size(), 2);
      {
        WavefrontObject wavefrontObject = objects.get(0);
        List<Integer> list = wavefrontObject.faces().stream() //
            .map(Tensor::length).distinct().collect(Collectors.toList());
        // contains quads and top/bottom polygon
        assertEquals(list, Arrays.asList(4, 32));
        Tensor normals = wavefrontObject.normals();
        Tensor faces = wavefrontObject.faces();
        normals.add(faces); // test if tensors have identical structure
        assertTrue(ExactTensorQ.of(faces));
        assertTrue(ExactTensorQ.of(normals));
      }
      {
        WavefrontObject wavefrontObject = objects.get(1);
        List<Integer> list = wavefrontObject.faces().stream() //
            .map(Tensor::length).distinct().collect(Collectors.toList());
        assertEquals(list, List.of(4));
        assertTrue(MatrixQ.of(wavefront.normals()));
        Tensor normals = wavefrontObject.normals();
        Tensor faces = wavefrontObject.faces();
        normals.add(faces); // test if tensors have identical structure
        Scalar index_max = (Scalar) normals.flatten(-1).reduce(Max::of).get();
        assertEquals(index_max.number().intValue() + 1, wavefront.normals().length());
      }
    }
  }

  @Test
  void testMathematica0() throws IOException {
    try (InputStream inputStream = getClass().getResource("/ch/alpine/sophus/obj/meshregionex2d.obj").openStream()) {
      Wavefront wavefront = WavefrontFormat.parse(ReadLine.of(inputStream));
      List<WavefrontObject> objects = wavefront.objects();
      assertEquals(objects.size(), 1);
      WavefrontObject wavefrontObject = objects.get(0);
      Tensor expect = Tensors.fromString("{{0, 1, 2}, {0, 1, 3}, {0, 1, 4}}");
      assertEquals(wavefrontObject.faces(), expect);
      Tensor vert = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, -1, 0}, {1, 0, 0}, {0, 1, 0}}");
      assertEquals(wavefront.vertices(), vert);
    }
  }
}
