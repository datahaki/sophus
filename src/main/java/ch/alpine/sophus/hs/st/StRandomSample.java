// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.pd.Orthogonalize;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** uniform distribution on St(n, k) according to Haar measure
 * 
 * References:
 * geomstats - stiefel.py
 * 
 * @param n positive
 * @param k non-negative, no greater than n
 * @return matrix that satisfies {@link OrthogonalMatrixQ} typically up to precision of 1e-10
 * 
 * @see StMemberQ */
public record StRandomSample(int n, int k) implements RandomSampleInterface, Serializable {
  public StRandomSample {
    Integers.requirePositive(n);
    if (k < 0 || n < k)
      throw new IllegalArgumentException("k=" + k);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), random, k, n);
    return Orthogonalize.usingPD(matrix);
    // Orthogonalize usingPD is identical to the geomstats formula:
    // Tensor aux = MatrixDotTranspose.of(matrix, matrix);
    // Tensor inv = MatrixSqrt.ofSymmetric(aux).sqrt_inverse();
    // return inv.dot(matrix);
  }
}
