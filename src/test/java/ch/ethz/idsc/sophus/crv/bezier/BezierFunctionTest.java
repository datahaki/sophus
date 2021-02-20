// code by jph
package ch.ethz.idsc.sophus.crv.bezier;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGeodesic;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.itp.BinaryAverage;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BezierFunctionTest extends TestCase {
  public void testSimple() {
    Tensor control = Tensors.fromString("{{0, 1}, {1, 0}, {2, 1}}");
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(RnGeodesic.INSTANCE, control);
    Tensor tensor = scalarTensorFunction.apply(RationalScalar.of(1, 4));
    assertEquals(tensor, Tensors.fromString("{1/2, 5/8}"));
    ExactTensorQ.require(tensor);
  }

  public void testRn() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}, {3, 1}}");
    ScalarTensorFunction stf1 = BezierFunction.of(RnGeodesic.INSTANCE, control);
    Scalar scalar = RationalScalar.of(1, 4);
    Tensor tensor = stf1.apply(scalar);
    assertEquals(tensor, Tensors.fromString("{3/4, 7/16}"));
    ExactTensorQ.require(tensor);
    Tensor domain = Subdivide.of(0, 1, 7);
    ScalarTensorFunction stf2 = BezierFunction.of(RnBiinvariantMean.INSTANCE, control);
    assertEquals(domain.map(stf1), domain.map(stf2));
  }

  public void testSe2Covering() {
    Tensor control = Tensors.fromString("{{0, 0, 0}, {1, 0, 1/2}, {2, 0.4, 2/5}}");
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(Se2CoveringGeodesic.INSTANCE, control);
    Scalar scalar = RationalScalar.of(1, 4);
    Tensor tensor = scalarTensorFunction.apply(scalar);
    assertEquals(tensor.Get(2), RationalScalar.of(17, 80));
    ExactScalarQ.require(tensor.Get(2));
  }

  public void testOutsideFail() {
    Tensor control = Tensors.fromString("{{0, 0, 0}, {1, 0, 1/2}, {2, 0.4, 2/5}}");
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(Se2CoveringGeodesic.INSTANCE, control);
    Scalar scalar = RationalScalar.of(-1, 4);
    Tensor tensor = scalarTensorFunction.apply(scalar);
    Chop._12.requireClose(tensor, Tensors.vector(-0.45359613406197646, 0.22282532025418184, -23 / 80.));
    ExactScalarQ.require(tensor.Get(2));
  }

  public void testSingleton() {
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(Se2CoveringGeodesic.INSTANCE, Array.zeros(1, 3));
    for (Tensor _x : Subdivide.of(-1, 2, 3 * 3)) {
      Tensor tensor = scalarTensorFunction.apply((Scalar) _x);
      assertEquals(tensor, Array.zeros(3));
      ExactTensorQ.require(tensor);
    }
  }

  public void testFailEmpty() {
    AssertFail.of(() -> BezierFunction.of(Se2CoveringGeodesic.INSTANCE, Tensors.empty()));
    AssertFail.of(() -> BezierFunction.of(RnBiinvariantMean.INSTANCE, Tensors.empty()));
  }

  public void testFailScalar() {
    AssertFail.of(() -> BezierFunction.of(Se2CoveringGeodesic.INSTANCE, RealScalar.ZERO));
  }

  public void testFailNull() {
    AssertFail.of(() -> BezierFunction.of((BiinvariantMean) null, Tensors.vector(1, 2, 3)));
    AssertFail.of(() -> BezierFunction.of((BinaryAverage) null, Tensors.vector(1, 2, 3)));
  }
}
