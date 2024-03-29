// code by jph
package ch.alpine.sophus.srf.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.ReadLine;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Sign;

class WavefrontTest {
  private static void check3d(File file) throws IOException {
    try (InputStream inputStream = new FileInputStream(file)) {
      Wavefront wavefront = WavefrontFormat.parse(ReadLine.of(inputStream));
      Tensor normals = wavefront.normals();
      assertEquals(Dimensions.of(normals).get(1), Integer.valueOf(3));
      Tensor vertices = wavefront.vertices();
      assertEquals(Dimensions.of(vertices).get(1), Integer.valueOf(3));
      List<WavefrontObject> objects = wavefront.objects();
      for (WavefrontObject wavefrontObject : objects) {
        Tensor faces = wavefrontObject.faces();
        if (0 < faces.length()) {
          CoordinateBoundingBox minMax = CoordinateBounds.of(faces);
          assertEquals(minMax.min().map(Sign::requirePositiveOrZero), minMax.min());
          ScalarUnaryOperator hi_bound = Min.function(RealScalar.of(vertices.length() - 1));
          assertEquals(minMax.max().map(hi_bound), minMax.max());
        }
        Tensor nrmls = wavefrontObject.normals();
        if (0 < nrmls.length()) {
          CoordinateBoundingBox minMax = CoordinateBounds.of(nrmls);
          assertEquals(minMax.min().map(Sign::requirePositiveOrZero), minMax.min());
          ScalarUnaryOperator hi_bound = Min.function(RealScalar.of(normals.length() - 1));
          assertEquals(minMax.max().map(hi_bound), minMax.max());
        }
      }
    }
  }

  @Test
  void testLoad() throws IOException {
    File directory = HomeDirectory.file("Projects", "gym-duckietown", "gym_duckietown", "meshes");
    if (directory.isDirectory())
      for (File file : directory.listFiles())
        if (file.getName().endsWith(".obj"))
          check3d(file);
  }
}
