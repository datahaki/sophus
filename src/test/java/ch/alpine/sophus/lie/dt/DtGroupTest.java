// code by jph
package ch.alpine.sophus.lie.dt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class DtGroupTest {
  private static final Exponential LIE_EXPONENTIAL = DtExponential.INSTANCE;
  private static final LieGroup LIE_GROUP = DtGroup.INSTANCE;

  @Test
  public void testSt1Inverse() {
    Tensor p = Tensors.fromString("{3, {6, 3}}");
    Tensor id = Tensors.fromString("{1, {0, 0}}");
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    DtGroupElement inv = pE.inverse();
    assertEquals(inv.toCoordinate(), Tensors.fromString("{1/3, {-2, -1}}"));
    assertEquals(inv.combine(p), id);
  }

  @Test
  public void testSt1Combine() {
    Tensor p = Tensors.fromString("{3, {6, 1}}");
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    Tensor q = Tensors.fromString("{2, {8, 5}}");
    assertEquals(pE.combine(q), Tensors.fromString("{6, {30, 16}}"));
  }

  @Test
  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new DtRandomSample(n, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
      Tensor g = RandomSample.of(rsi); // element
      RandomSampleInterface tsi = new TDtRandomSample(n, UniformDistribution.of(-1, 1));
      Tensor x = RandomSample.of(tsi); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new DtRandomSample(n, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
      Tensor g = RandomSample.of(rsi);
      Tensor m = RandomSample.of(rsi);
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }
}
