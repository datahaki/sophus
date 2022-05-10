// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class Se2CoveringGeodesicTest {
  @Test
  public void testArticle() {
    Tensor tensor = Se2CoveringGroup.INSTANCE.split( //
        Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), RealScalar.of(0.7));
    Chop._14.requireClose(tensor, Tensors.fromString("{4.483830852817113, 3.2143505344919467, 5.1}"));
  }

  @Test
  public void testCenter() {
    // mathematica gives: {2.26033, -0.00147288, 0.981748}
    Tensor p = Tensors.vector(2.017191762967754, -0.08474511292102775, 0.9817477042468103);
    Tensor q = Tensors.vector(2.503476971090440, +0.08179934782700435, 0.9817477042468102);
    Scalar scalar = RealScalar.of(0.5);
    Tensor delta = new Se2CoveringGroupElement(p).inverse().combine(q);
    Tensor x = Se2CoveringGroup.INSTANCE.log(delta).multiply(scalar);
    x.get();
    // x= {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    // mathematica gives
    // x= {0.204321, .......... -0.155902, ......... -5.55112*10^-17}
    // Tensor exp_x = Se2CoveringIntegrator.INSTANCE.spin(Tensors.vector(0, 0, 0), x);
    // exp_x == {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    Tensor tensor = Se2CoveringGroup.INSTANCE.split(p, q, scalar);
    // {2.260334367029097, -0.0014728825470118057, 0.9817477042468103}
    Chop._14.requireClose(tensor, //
        Tensors.fromString("{2.260334367029097, -0.0014728825470118057, 0.9817477042468103}"));
  }

  @Test
  public void testBiinvariantMean() {
    Distribution distribution = UniformDistribution.of(-3, 8);
    Distribution wd = UniformDistribution.unit();
    int success = 0;
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar w = RandomVariate.of(wd);
      Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
      Tensor splt = Se2CoveringGroup.INSTANCE.split(p, q, w);
      if (Chop._12.isClose(mean, splt))
        ++success;
    }
    assertTrue(90 < success);
  }
}
