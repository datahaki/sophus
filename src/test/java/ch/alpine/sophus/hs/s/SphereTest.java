package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.RandomSample;

class SphereTest {
  @Test
  void test() {
    Sphere sphere = new Sphere(3);
    assertEquals(sphere.toString(), "S[3]");
  }

  @Test
  void test2() throws ClassNotFoundException, IOException {
    Sphere sphereN = new Sphere(2);
    Serialization.copy(sphereN);
    assertEquals(sphereN.toString(), "S[2]");
  }

  @Test
  void testSimple() {
    Tensor tensor = RandomSample.of(new Sphere(1), 10);
    assertEquals(Dimensions.of(tensor), Arrays.asList(10, 2));
  }
}
