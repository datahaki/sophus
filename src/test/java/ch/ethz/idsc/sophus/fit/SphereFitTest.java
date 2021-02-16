// code by jph
package ch.ethz.idsc.sophus.fit;

import java.io.IOException;
import java.util.Optional;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SphereFitTest extends TestCase {
  public void testToString() {
    Tensor points = Tensors.of( //
        Tensors.vector(1, 0), //
        Tensors.vector(0, 0), //
        Tensors.vector(0, 1)).unmodifiable();
    Optional<SphereFit> optional = SphereFit.of(points);
    SphereFit sphereFit = optional.get();
    String string = sphereFit.toString();
    assertTrue(string.startsWith("SphereFit[center={1/2, 1/2}, radius=0.7071067811"));
    assertTrue(string.endsWith("]"));
  }

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

  public void testRank() {
    Distribution distribution = UniformDistribution.unit();
    for (int dim = 3; dim < 5; ++dim)
      for (int count = 1; count < 10; ++count) {
        Tensor points = RandomVariate.of(distribution, count, dim);
        Optional<SphereFit> optional = SphereFit.of(points);
        assertEquals(optional.isPresent(), dim < count);
      }
  }

  public void testSingle() {
    Tensor points = Tensors.of(Tensors.vector(1, 2, 3));
    Optional<SphereFit> optional = SphereFit.of(points);
    assertFalse(optional.isPresent()); // because a single point is co-linear
  }

  public void testFailEmpty() {
    AssertFail.of(() -> SphereFit.of(Tensors.empty()));
  }

  public void testFailScalar() {
    AssertFail.of(() -> SphereFit.of(RealScalar.ONE));
  }

  public void testFailRank3() {
    AssertFail.of(() -> SphereFit.of(LeviCivitaTensor.of(3)));
  }

  public void testFailUnstructured1() {
    AssertFail.of(() -> SphereFit.of(Tensors.fromString("{{1, 2, 3}, {4, -5}}")));
  }

  public void testFailUnstructured2() {
    AssertFail.of(() -> SphereFit.of(Tensors.fromString("{{1, 2, 3}, {4, -5}, {6, 7, 8}}")));
  }
}
