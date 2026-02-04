// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.GroupCheck;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Tuples;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class Se2GroupTest {
  @Test
  void testSimple() {
    Tensor tensor = Se2Group.INSTANCE.combine(Tensors.vector(1, 2, 2 * Math.PI + 3), Tensors.vector(0, 0, 0));
    assertEquals(tensor, Tensors.vector(1, 2, 3));
    GroupCheck.check(Se2Group.INSTANCE);
  }

  @Test
  void testWrap() {
    Tensor xya = Se2Group.INSTANCE.exponential0().exp(Tensors.vector(0, 0, 4));
    Sign.requirePositive(xya.Get(2).negate());
  }

  @Test
  void testSimple2() {
    Tensor split = Se2Group.INSTANCE.split(Tensors.vector(0, 0, 0), Tensors.vector(10, 0, 1), RealScalar.of(0.7));
    Chop._13.requireClose(split, Tensors.fromString("{7.071951896570488, -1.0688209919859546, 0.7}"));
  }

  @Test
  void testModPi() {
    Tensor split = Se2Group.INSTANCE.split(Tensors.vector(0, 0, 10), Tensors.vector(0, 0, 10), RealScalar.of(0.738));
    Chop._13.requireClose(split, Tensors.of(RealScalar.ZERO, RealScalar.ZERO, So2.MOD.apply(RealScalar.of(10))));
  }

  @Test
  void testBiinvariantMean() {
    RandomGenerator random = new Random(1);
    Distribution distribution = UniformDistribution.of(-10, 8);
    Distribution wd = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomVariate.of(distribution, random, 3);
      Tensor q = RandomVariate.of(distribution, random, 3);
      Scalar w = RandomVariate.of(wd, random);
      Tensor mean = Se2BiinvariantMeans.FILTER.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
      Tensor splt = Se2Group.INSTANCE.split(p, q, w);
      splt.set(So2.MOD, 2);
      Chop._12.requireClose(mean, splt);
    }
  }

  @Test
  void testUnits() {
    Tensor p = Tensors.fromString("{4.9[m], 4.9[m], 0.9}");
    Tensor q = Tensors.fromString("{5.0[m], 5.0[m], 1.0}");
    Tensor r = Tensors.fromString("{5.1[m], 5.1[m], 1.1}");
    Tensor s = Tensors.fromString("{4.8[m], 5.2[m], 1.3}");
    for (Tensor pair : Tuples.of(Tensors.of(p, q, r, s), 2))
      Se2Group.INSTANCE.combine(pair.get(0), pair.get(1));
  }

  @Test
  void testUnitsMatrix() {
    Tensor p = Tensors.fromString("{{2,3,4[m]},{1,1,7[m]},{0[m^-1],0[m^-1],1}}");
    Tensor q = Tensors.fromString("{{1,2,4[m]},{-1,3,1[m]},{0[m^-1],0[m^-1],1}}");
    Tensor result = p.dot(q);
    Tensor expect = Tensors.fromString("{{-1, 13, 15[m]}, {0, 5, 12[m]}, {0[m^-1], 0[m^-1], 1}}");
    assertEquals(result, expect);
  }

  @Test
  void testSimple23() {
    Tensor tensor = Se2Group.INSTANCE.combine(Tensors.vector(1, 2, 3), Tensors.vector(6, 7, 8));
    assertTrue(Sign.isNegative(tensor.Get(2)));
  }

  @Test
  void testRotationFixpointSideLeft() {
    Tensor element = Se2Group.INSTANCE.invert(Tensors.vector(0, 1, 0)); // "left rear wheel"
    Tensor tensor = Se2Group.INSTANCE.adjoint(element, Tensors.vector(1, 0, 1)); // more forward and turn left
    Chop._13.requireClose(tensor, UnitVector.of(3, 2)); // only rotation
  }

  @Test
  void testRotationSideLeft() {
    Tensor element = Tensors.vector(0, 1, 0); // "left rear wheel"
    Tensor tensor = Se2Group.INSTANCE.adjoint(element, Tensors.vector(1, 0, -1)); // more forward and turn right
    Chop._13.requireClose(tensor, UnitVector.of(3, 2).negate()); // only rotation
  }

  @Test
  void testRotationFixpointSideRight() {
    Tensor element = Tensors.vector(0, -1, 0); // "right rear wheel"
    Tensor tensor = Se2Group.INSTANCE.adjoint(element, Tensors.vector(1, 0, -1)); // more forward and turn right
    Chop._13.requireClose(tensor, Tensors.vector(2, 0, -1)); // rotate and translate
  }

  @Test
  void testRotationId() {
    Tensor element = Tensors.vector(0, 0, 2);
    Tensor tensor = Se2Group.INSTANCE.adjoint(element, Tensors.vector(0, 0, 1));
    Chop._13.requireClose(tensor, UnitVector.of(3, 2)); // same rotation
  }

  @Test
  void testRotationTranslation() {
    Tensor element = Tensors.vector(1, 0, Math.PI / 2);
    Tensor tensor = Se2Group.INSTANCE.adjoint(element, Tensors.vector(0, 0, 1));
    Chop._13.requireClose(tensor, Tensors.vector(0, -1, 1));
  }

  @Test
  void testTranslate() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor element = RandomVariate.of(distribution, 2).append(RealScalar.ZERO);
      Tensor uvw = RandomVariate.of(distribution, 2).append(RealScalar.ZERO);
      Tensor tensor = Se2Group.INSTANCE.adjoint(element, uvw);
      Chop._13.requireClose(tensor, uvw); // only translation
    }
  }

  @Test
  void testComparison() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      // Se2GroupElement element = Se2Group.INSTANCE.element(xya);
      Se2AdjointComp se2AdjointComp = new Se2AdjointComp(xya);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Chop._13.requireClose(Se2Group.INSTANCE.adjoint(xya, uvw), se2AdjointComp.apply(uvw)); // only translation
    }
  }

  @Test
  void testFwdInv() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      // Se2GroupElement element = Se2Group.INSTANCE.element(xya);
      TensorUnaryOperator inverse = X -> Se2Group.INSTANCE.adjoint(Se2Group.INSTANCE.invert(xya), X);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Chop._13.requireClose(inverse.apply(Se2Group.INSTANCE.adjoint(xya, uvw)), uvw);
      Chop._13.requireClose(Se2Group.INSTANCE.adjoint(xya, inverse.apply(uvw)), uvw);
    }
  }

  @Test
  void testNonCovering() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor res = Se2Group.INSTANCE.adjoint(xya, uvw);
      for (int v = -3; v <= 3; ++v) {
        Tensor xyp = xya.copy();
        xyp.set(RealScalar.of(v * 2 * Math.PI)::add, 2);
        Chop._13.requireClose(res, Se2Group.INSTANCE.adjoint(xyp, uvw));
      }
    }
  }

  @Test
  void testSimple22() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      TensorUnaryOperator se2Adjoint = X -> Se2Group.INSTANCE.adjoint(g, X);
      Tensor u_w = RandomVariate.of(distribution, 3);
      Tensor out = se2Adjoint.apply(u_w);
      assertEquals(Dimensions.of(out), List.of(3));
      Tensor g_i = Se2Group.INSTANCE.diffOp(g).apply(Array.zeros(3));
      TensorUnaryOperator se2Inverse = X -> Se2Group.INSTANCE.adjoint(g_i, X);
      Tensor apply = se2Inverse.apply(out);
      Chop._13.requireClose(u_w, apply);
    }
  }

  @Test
  void testUnits123() {
    TensorUnaryOperator se2Adjoint = X -> Se2Group.INSTANCE.adjoint(Tensors.fromString("{2[m], 3[m], 4}"), X);
    Tensor tensor = se2Adjoint.apply(Tensors.fromString("{7[m*s^-1], -5[m*s^-1], 1[s^-1]}"));
    Chop._13.requireClose(tensor, //
        Tensors.fromString("{-5.359517822584925[m*s^-1], -4.029399362837438[m*s^-1], 1[s^-1]}"));
  }

  @Test
  void testLinearGroupSe2() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor adjoint = Se2Group.INSTANCE.adjoint(g, uvw);
      Tensor gM = Se2Matrix.of(g);
      Tensor X = Tensors.matrix(new Scalar[][] { //
          { RealScalar.ZERO, uvw.Get(2).negate(), uvw.Get(0) }, //
          { uvw.Get(2), RealScalar.ZERO, uvw.Get(1) }, //
          { RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO } });
      Tensor tensor = gM.dot(X).dot(Inverse.of(gM));
      Tensor xya = Tensors.of(tensor.Get(0, 2), tensor.Get(1, 2), tensor.Get(1, 0));
      Chop._12.requireClose(adjoint, xya);
    }
  }

  @Test
  void testDL() {
    Tensor g = Tensors.vector(0, 0, Math.PI / 2);
    Tensor dL = Se2Group.INSTANCE.dL(g, Tensors.vector(1, 0, 0));
    Chop._12.requireClose(dL, UnitVector.of(3, 1).negate());
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> Se2Adjoint.forward(RealScalar.ONE));
    assertThrows(Exception.class, () -> Se2Adjoint.forward(HilbertMatrix.of(3)));
    assertThrows(Exception.class, () -> Se2Adjoint.inverse(RealScalar.ONE));
    assertThrows(Exception.class, () -> Se2Adjoint.inverse(HilbertMatrix.of(3)));
  }

  @Test
  void testDlNullFail() {
    Tensor p = Tensors.vector(0, 0, Math.PI / 2);
    assertThrows(Exception.class, () -> Se2Group.INSTANCE.dL(p, null));
  }
}
