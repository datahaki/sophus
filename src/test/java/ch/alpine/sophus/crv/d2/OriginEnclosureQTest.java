// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.qty.Unit;

class OriginEnclosureQTest {
  @Test
  public void testInsidePlain() {
    Tensor polygon = Tensors.matrix(new Number[][] { //
        { 0.1, 0.1 }, //
        { 1, 0 }, //
        { 1, 1 }, //
        { 0, 1 } //
    });
    assertFalse(OriginEnclosureQ.INSTANCE.test(polygon));
    for (int n = 3; n < 10; ++n) {
      assertTrue(OriginEnclosureQ.INSTANCE.test(CirclePoints.of(n)));
      assertTrue(OriginEnclosureQ.INSTANCE.test(Reverse.of(CirclePoints.of(n))));
    }
  }

  @Test
  public void testInsidePlainQuantity() {
    ScalarUnaryOperator suo = Scalars.attach(Unit.of("km"));
    Tensor polygon = Tensors.matrix(new Number[][] { //
        { 0.1, 0.1 }, //
        { 1, 0 }, //
        { 1, 1 }, //
        { 0, 1 } //
    });
    assertFalse(OriginEnclosureQ.INSTANCE.test(polygon.map(suo)));
    for (int n = 3; n < 10; ++n) {
      assertTrue(OriginEnclosureQ.INSTANCE.test(CirclePoints.of(n).map(suo)));
      assertTrue(OriginEnclosureQ.INSTANCE.test(Reverse.of(CirclePoints.of(n)).map(suo)));
    }
  }

  @Test
  public void testSome() {
    Tensor asd = Tensors.vector(2, 3, 4, 5);
    asd.set(RealScalar.of(8), 1);
    assertEquals(asd.Get(1), RealScalar.of(8));
    List<Integer> list = new ArrayList<>();
    list.add(6);
    list.add(2);
    list.add(3);
    list.add(9);
    list.get(1).longValue();
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> OriginEnclosureQ.INSTANCE.test(IdentityMatrix.of(4)));
  }

  @Test
  public void testCPointers() {
    {
      String wer = "asdf";
      String wer2 = wer;
      wer = "987345"; // does not change wer2
      assertFalse(wer.equals(wer2));
    }
    {
      Tensor wo = Tensors.vector(2, 3, 4, 5);
      Tensor wo2 = wo;
      wo = Tensors.vector(9, 9); // does not change wo2
      assertFalse(wo.equals(wo2));
    }
  }

  @Test
  public void testScalarFail() {
    assertThrows(Exception.class, () -> FranklinPnpoly.isInside(RealScalar.of(2), Tensors.vector(0.5, .5)));
  }
}
