// code by jph
package ch.alpine.sophus.lie.he;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.api.VectorEncodingMarker;
import ch.alpine.sophus.lie.SpecificGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class HeNGroup extends HeGroup implements SpecificGroup, VectorEncodingMarker {
  private final int n;

  public HeNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return uvw -> VectorQ.of(uvw) && dimensions() == uvw.length();
  }

  @Override
  public int dimensions() {
    return n * 2 + 1;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, dimensions());
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }

  @Override
  public int matrixOrder() {
    return n + 2;
  }
}
