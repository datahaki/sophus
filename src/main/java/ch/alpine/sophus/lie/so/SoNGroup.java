// code by jph
package ch.alpine.sophus.lie.so;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.alg.Subsets;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.lie.rot.RotationMatrix;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * for n == 2 and n == 3 there are faster alternatives
 * 
 * consistent with {@link RotationMatrix} and {@link So3Exponential} */
public class SoNGroup extends SoGroup implements SpecificManifold, MatrixGroup {
  private final int n;

  public SoNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  public MatrixAlgebra matrixAlgebra() {
    LinearSubspace linearSubspace = LinearSubspace.of(TSoMemberQ.INSTANCE::defect, n, n);
    return new MatrixAlgebra(linearSubspace.basis());
  }

  @Override
  public Tensor matrixBasis() {
    Tensor basis = Tensors.empty();
    Scalar parity = RealScalar.ONE;
    for (Tensor ij : Subsets.of(Reverse.of(Range.of(0, n)), 2)) {
      int[] index = Primitives.toIntArray(ij);
      int i = index[0];
      int j = index[1];
      Tensor elem = Array.sparse(n, n);
      elem.set(parity, i, j);
      parity = parity.negate();
      elem.set(parity, j, i);
      basis.append(elem);
    }
    return basis;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    /* for some reason this always gives Det == 1 */
    return QRDecomposition.of(RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, n)) //
        .getQConjugateTranspose();
  }

  @Override
  public int dimensions() {
    return n * (n - 1) / 2;
  }

  @Override
  public final String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
