// code by jph
package ch.alpine.sophus.lie.so;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.SpecificLieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.lie.rot.RotationMatrix;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * for n == 2 and n == 3 there are faster alternatives
 * 
 * consistent with {@link RotationMatrix} and {@link So3Exponential} */
public class SoNGroup extends SoGroup implements SpecificLieGroup {
  private final int n;

  public SoNGroup(int n) {
    this.n = Integers.requirePositive(n);
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
  public int matrixOrder() {
    return n;
  }

  @Override
  public final String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
