// code by jph
package ch.alpine.sophus.hs.h;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class Hyperboloid extends HManifold implements SpecificManifold {
  private final int dimensions;

  public Hyperboloid(int dimensions) {
    this.dimensions = dimensions;
  }

  @Override
  public MemberQ isPointQ() {
    return VectorQ.ofLength(dimensions());
  }

  @Override
  public int dimensions() {
    return dimensions;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, dimensions());
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), dimensions);
  }
}
