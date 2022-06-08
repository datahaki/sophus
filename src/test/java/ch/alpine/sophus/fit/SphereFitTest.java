// code by jph
package ch.alpine.sophus.fit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class SphereFitTest {
  @Test
  public void testToString() {
    Tensor points = Tensors.of( //
        Tensors.vector(1, 0), //
        Tensors.vector(0, 0), //
        Tensors.vector(0, 1)).unmodifiable();
    Optional<SphereFit> optional = SphereFit.of(points);
    SphereFit sphereFit = optional.get();
    String string = sphereFit.toString();
    assertTrue(string.startsWith("SphereFit["));
    assertTrue(string.endsWith("]"));
  }

  @Test
  public void testLinearSolve() {
    Tensor _points = Tensors.of( //
        Tensors.vector(1, 0), //
        Tensors.vector(0, 0), //
        Tensors.vector(0, 1)).unmodifiable();
    for (Tensor shift : Tensors.fromString("{{0, 0}, {0, -5/3}, {10, 0}, {20, 20}}")) {
      Tensor points = Tensor.of(_points.stream().map(shift::add));
      Optional<SphereFit> optional = SphereFit.of(points);
      SphereFit sphereFit = optional.get();
      Tensor center = sphereFit.center();
      Tensor radius = sphereFit.radius();
      assertEquals(center, Tensors.vector(0.5, 0.5).add(shift));
      ExactTensorQ.require(center);
      Chop._13.requireClose(radius, RealScalar.of(Math.sqrt(0.5)));
    }
  }

  @Test
  public void testLeastSquare() throws ClassNotFoundException, IOException {
    Tensor _points = Tensors.of( //
        Tensors.vector(1, 0), //
        Tensors.vector(0, 0), //
        Tensors.vector(0, 1), //
        Tensors.vector(0.8, 0.7));
    for (Tensor shift : Tensors.fromString("{{0, 0}, {0, -5/3}, {10, 0}, {20, 20}}")) {
      Tensor points = Tensor.of(_points.stream().map(shift::add));
      Optional<SphereFit> optional = SphereFit.of(points);
      SphereFit sphereFit = Serialization.copy(optional.get());
      Tensor center = sphereFit.center();
      Tensor radius = sphereFit.radius();
      Chop._13.requireClose(center, Tensors.vector(0.39894957983193285, 0.4067226890756306).add(shift));
      Chop._09.requireClose(radius, RealScalar.of(0.6342832218291473));
    }
  }

  @Test
  public void testLowRank() {
    Tensor _points = Tensors.of( //
        Tensors.vector(0, 0), //
        Tensors.vector(0, 0), //
        Tensors.vector(0, 1)).unmodifiable();
    for (Tensor shift : Tensors.fromString("{{0, 0}, {0, -5/3}, {10, 0}, {20, 20}}")) {
      Tensor points = Tensor.of(_points.stream().map(shift::add));
      Optional<SphereFit> optional = SphereFit.of(points);
      // SphereFit sphereFit = optional.get();
      // System.out.println(sphereFit);
      assertFalse(optional.isPresent());
    }
  }

  @Test
  public void testRank() {
    Distribution distribution = UniformDistribution.unit();
    for (int dim = 3; dim < 5; ++dim)
      for (int count = 1; count < 10; ++count) {
        Tensor points = RandomVariate.of(distribution, count, dim);
        Optional<SphereFit> optional = SphereFit.of(points);
        assertEquals(optional.isPresent(), dim < count);
      }
  }

  @Test
  public void testSingle() {
    Tensor points = Tensors.of(Tensors.vector(1, 2, 3));
    Optional<SphereFit> optional = SphereFit.of(points);
    assertFalse(optional.isPresent()); // because a single point is co-linear
  }

  @Test
  public void testFailEmpty() {
    assertThrows(Exception.class, () -> SphereFit.of(Tensors.empty()));
  }

  @Test
  public void testFailScalar() {
    assertThrows(Exception.class, () -> SphereFit.of(RealScalar.ONE));
  }

  @Test
  public void testFailRank3() {
    assertThrows(Exception.class, () -> SphereFit.of(LeviCivitaTensor.of(3)));
  }

  @Test
  public void testFailUnstructured1() {
    assertThrows(Exception.class, () -> SphereFit.of(Tensors.fromString("{{1, 2, 3}, {4, -5}}")));
  }

  @Test
  public void testFailUnstructured2() {
    assertThrows(Exception.class, () -> SphereFit.of(Tensors.fromString("{{1, 2, 3}, {4, -5}, {6, 7, 8}}")));
  }
}
