// code by jph
package ch.alpine.sophus.hs.h;

import ch.alpine.sophus.api.SpecificManifold;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class Hyperboloid extends HManifold implements SpecificManifold {
  private final int dimensions;
  private final ZeroDefectArrayQ isPointQ;

  public Hyperboloid(int dimensions) {
    this.dimensions = dimensions;
    isPointQ = VectorQ.ofLength(dimensions());
  }

  @Override
  public MemberQ isPointQ() {
    return isPointQ;
  }

  @Override
  public int dimensions() {
    return dimensions;
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return RandomVariate.array(NormalDistribution.standard(), dimensions);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), dimensions);
  }
}
