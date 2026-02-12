// code by jph
package ch.alpine.sophus.lie.sp;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.sca.Chop;

public class Symplectic extends GlGroup {
  private final int n;
  private final Tensor omega;

  public Symplectic(int n) {
    this.n = n;
    this.omega = SymplecticForm.omega(n);
  }

  @Override
  public MemberQ isPointQ() {
    return m -> Chop._08.allZero(Dot.of(Transpose.of(m), omega, m).subtract(omega));
  }

  public MatrixAlgebra matrixAlgebra() {
    int m = n + 1;
    LinearSubspace linearSubspace = LinearSubspace.of(new TSpMemberQ(n)::defect, m, m);
    return new MatrixAlgebra(linearSubspace.basis());
  }

  public int dimensions() {
    return n * (2 * n + 1);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Sp", "2*" + omega.length() / 2);
  }
}
