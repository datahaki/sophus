// code by jph
package ch.alpine.sophus.hs.h;

import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;

public class Hyperboloid extends HManifold {
  private final int dimensions;

  public Hyperboloid(int dimensions) {
    this.dimensions = dimensions;
  }

  @Override
  public MemberQ isPointQ() {
    return VectorQ.ofLength(dimensions);
  }

  @Override
  public int dimensions() {
    return dimensions;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("H", dimensions);
  }
}
