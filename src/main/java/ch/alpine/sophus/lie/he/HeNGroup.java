// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.api.VectorEncodingMarker;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class HeNGroup extends HeGroup implements VectorEncodingMarker {
  private final int n;
  private final ZeroDefectArrayQ isPointQ;

  public HeNGroup(int n) {
    this.n = Integers.requirePositive(n);
    isPointQ = VectorQ.ofLength(dimensions());
  }

  @Override
  public MemberQ isPointQ() {
    return isPointQ;
  }

  @Override
  public int dimensions() {
    return n * 2 + 1;
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return RandomVariate.array(NormalDistribution.standard(), dimensions());
  }

  @Override
  public int matrixOrder() {
    return n + 2;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
