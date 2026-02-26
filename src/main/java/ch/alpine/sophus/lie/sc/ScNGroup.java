// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.api.VectorEncodingMarker;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;

public class ScNGroup extends ScGroup implements VectorEncodingMarker {
  private final int n;

  public ScNGroup(int n) {
    this.n = n;
  }

  @Override
  public MemberQ isPointQ() {
    return t -> super.isPointQ().test(t) && t.length() == n;
  }

  @Override
  public int dimensions() {
    return n;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Sc", n);
  }
}
