// code by jph
package ch.alpine.sophus.lie.sp;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.sca.Chop;

public class SpGroup extends GlGroup {
  @Override
  public MemberQ isPointQ() {
    return m -> {
      Tensor omega = new SymplecticForm(m.length() / 2).matrix();
      return Chop._08.allZero(Dot.of(Transpose.of(m), omega, m).subtract(omega));
    };
  }

  @Override
  public final Exponential exponential0() {
    return SpExponential.INSTANCE;
  }

  @Override
  public String toString() {
    return "Sp";
  }
}
