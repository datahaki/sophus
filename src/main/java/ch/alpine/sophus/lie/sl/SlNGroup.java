// code by jph
package ch.alpine.sophus.lie.sl;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Power;

public class SlNGroup extends SlGroup implements SpecificManifold, MatrixGroup {
  private final int n;

  public SlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> matrix.length() == n //
        && super.isPointQ().test(matrix);
  }

  public MatrixAlgebra matrixAlgebra() {
    LinearSubspace linearSubspace = LinearSubspace.of(TSlMemberQ.INSTANCE::defect, n, n);
    return new MatrixAlgebra(linearSubspace.basis());
  }

  @Override
  public Tensor matrixBasis() {
    Tensor basis = Tensors.empty();
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        {
          Tensor tensor = Array.sparse(n, n);
          tensor.set(RealScalar.ONE, i, j);
          tensor.set(RealScalar.ONE.negate(), j, i);
          basis.append(tensor);
        }
        {
          Tensor tensor = Array.sparse(n, n);
          tensor.set(RealScalar.ONE, i, j);
          tensor.set(RealScalar.ONE, j, i);
          basis.append(tensor);
        }
      }
    for (int i = 1; i < n; ++i) {
      Tensor tensor = Array.sparse(n, n);
      tensor.set(RealScalar.ONE, i - 1, i - 1);
      tensor.set(RealScalar.ONE.negate(), i, i);
      basis.append(tensor);
    }
    return basis.multiply(Rational.HALF);
  }

  @Override
  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, n);
    Scalar det = Det.of(matrix);
    if (Sign.isNegative(det)) {
      matrix.set(Tensor::negate, 0);
      det = det.negate();
    }
    return matrix.divide(Power.of(det, Rational.of(1, n)));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
