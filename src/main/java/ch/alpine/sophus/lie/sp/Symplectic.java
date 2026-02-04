package ch.alpine.sophus.lie.sp;

import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.spa.SparseArray;

public class Symplectic extends GlGroup {
  public static Tensor omega(int n) {
    Tensor omega = SparseArray.of(RealScalar.ZERO, 2 * n, 2 * n);
    for (int k = 0; k < n; ++k) {
      omega.set(RealScalar.ONE, k, n + k);
      omega.set(RealScalar.ONE.negate(), n + k, k);
    }
    return omega;
  }

  private final Tensor omega;

  public Symplectic(int n) {
    omega = omega(n);
  }

  @Override
  public boolean isMember(Tensor m) {
    return Chop._08.allZero(Dot.of(Transpose.of(m), omega, m).subtract(omega));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Sp", "2*" + omega.length() / 2);
  }
}
