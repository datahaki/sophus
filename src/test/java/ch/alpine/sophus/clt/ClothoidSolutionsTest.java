// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.sophus.clt.ClothoidSolutions.Search;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Sort;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Mod;
import junit.framework.TestCase;

public class ClothoidSolutionsTest extends TestCase {
  private static final ClothoidSolutions CLOTHOID_SOLUTIONS = ClothoidSolutions.of(Clips.absolute(15.0));
  private static final int ATTEMPTS = 3;

  public void testS1Odd() {
    Search cs1 = CLOTHOID_SOLUTIONS.new Search(RealScalar.of(+0.1), RealScalar.of(0.3));
    Search cs2 = CLOTHOID_SOLUTIONS.new Search(RealScalar.of(-0.1), RealScalar.of(0.3));
    Tensor sol1 = cs1.lambdas();
    Tensor sol2 = Sort.of(cs2.lambdas().negate());
    Chop._04.requireClose(sol1, sol2);
  }

  public void testS2Even() {
    // Search cs1 = CLOTHOID_SOLUTIONS.new Search(RealScalar.of(+0.1),
    // RealScalar.of(+0.3));
    // Search cs2 = CLOTHOID_SOLUTIONS.new Search(RealScalar.of(-0.1),
    // RealScalar.of(-0.3));
    // ClothoidSolutions cs1 = ClothoidSolutions.of(+0.1, +0.3);
    // ClothoidSolutions cs2 = ClothoidSolutions.of(+0.1, -0.3);
    // Tensor sol1 = cs1.lambdas();
    // Tensor sol2 = cs2.lambdas();
    // Chop._04.requireClose(sol1, sol2);
  }

  public void testRandomS1Odd() {
    Distribution distribution = UniformDistribution.of(-20, 20);
    for (int count = 0; count < ATTEMPTS; ++count) {
      Scalar s1 = RandomVariate.of(distribution);
      Scalar s2 = RandomVariate.of(distribution);
      Search cs1 = CLOTHOID_SOLUTIONS.new Search(s1, s2);
      Search cs2 = CLOTHOID_SOLUTIONS.new Search(s1.negate(), s2);
      // ClothoidSolutions cs1 = ClothoidSolutions.of(s1, s2);
      // ClothoidSolutions cs2 = ClothoidSolutions.of(s1.negate(), s2);
      Tensor sol1 = cs1.lambdas();
      Tensor sol2 = Sort.of(cs2.lambdas().negate());
      Chop._04.requireClose(sol1, sol2);
    }
  }

  public void testRandomS2Even() {
    Distribution distribution = UniformDistribution.of(-20, 20);
    for (int count = 0; count < ATTEMPTS; ++count) {
      Scalar s1 = RandomVariate.of(distribution);
      Scalar s2 = RandomVariate.of(distribution);
      Search cs1 = CLOTHOID_SOLUTIONS.new Search(s1, s2);
      Search cs2 = CLOTHOID_SOLUTIONS.new Search(s1, s2.negate());
      // ClothoidSolutions cs1 = ClothoidSolutions.of(s1, s2);
      // ClothoidSolutions cs2 = ClothoidSolutions.of(s1, s2.negate());
      Tensor sol1 = cs1.lambdas();
      Tensor sol2 = cs2.lambdas();
      Chop._04.requireClose(sol1, sol2);
    }
  }

  public void testS1Period2Pi() {
    Distribution distribution = UniformDistribution.of(-20, 20);
    for (int count = 0; count < ATTEMPTS; ++count) {
      Scalar s1 = RandomVariate.of(distribution);
      Scalar s2 = RandomVariate.of(distribution);
      Search cs1 = CLOTHOID_SOLUTIONS.new Search(s1, s2);
      Search cs2 = CLOTHOID_SOLUTIONS.new Search(s1.add(Pi.TWO), s2);
      // ClothoidSolutions cs1 = ClothoidSolutions.of(s1, s2);
      // ClothoidSolutions cs2 = ClothoidSolutions.of(s1.add(Pi.TWO), s2);
      Tensor sol1 = cs1.lambdas();
      Tensor sol2 = cs2.lambdas();
      Chop._04.requireClose(sol1, sol2);
    }
  }

  public void testS1Period2PiMod() {
    Distribution distribution = UniformDistribution.of(-20, 20);
    for (int count = 0; count < ATTEMPTS; ++count) {
      Scalar s1 = RandomVariate.of(distribution);
      Scalar s2 = RandomVariate.of(distribution);
      Search cs1 = CLOTHOID_SOLUTIONS.new Search(s1, s2);
      Search cs2 = CLOTHOID_SOLUTIONS.new Search(Mod.function(Pi.TWO).apply(s1), s2);
      // ClothoidSolutions cs1 = ClothoidSolutions.of(s1, s2);
      // ClothoidSolutions cs2 = ClothoidSolutions.of(Mod.function(Pi.TWO).apply(s1),
      // s2);
      Tensor sol1 = cs1.lambdas();
      Tensor sol2 = cs2.lambdas();
      Chop._04.requireClose(sol1, sol2);
    }
  }

  public void testS1Period2PiMirror() {
    Distribution distribution = UniformDistribution.of(-20, 20);
    for (int count = 0; count < ATTEMPTS; ++count) {
      Scalar s1 = RandomVariate.of(distribution);
      Scalar s2 = RandomVariate.of(distribution);
      Search cs1 = CLOTHOID_SOLUTIONS.new Search(s1, s2);
      // ClothoidSolutions cs1 = ClothoidSolutions.of(s1, s2);
      Scalar r1 = Mod.function(Pi.TWO).apply(s1);
      boolean mirror = Scalars.lessThan(Pi.VALUE, r1);
      if (mirror)
        r1 = Pi.TWO.subtract(r1);
      assertTrue(Scalars.lessThan(r1, Pi.VALUE));
      Search cs2 = CLOTHOID_SOLUTIONS.new Search(r1, s2);
      // ClothoidSolutions cs2 = ClothoidSolutions.of(r1, s2);
      Tensor sol1 = cs1.lambdas();
      if (mirror)
        Chop._04.requireClose(sol1, Sort.of(cs2.lambdas().negate()));
      else
        Chop._04.requireClose(sol1, cs2.lambdas());
    }
  }
}
