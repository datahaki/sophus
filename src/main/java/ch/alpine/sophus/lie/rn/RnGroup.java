// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.api.VectorEncodingMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** additive group of vectors of length n */
public class RnGroup extends RGroup implements VectorEncodingMarker {
  private final int n;
  private final ZeroDefectArrayQ isPointQ;

  /** @param n vector length */
  public RnGroup(int n) {
    this.n = n;
    isPointQ = VectorQ.ofLength(dimensions());
  }

  @Override
  public MemberQ isPointQ() {
    return isPointQ;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, dimensions());
  }

  @Override
  public int dimensions() {
    return n;
  }

  @Override
  public int matrixOrder() {
    return n + 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
