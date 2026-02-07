// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.lie.JacobiIdentity;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.LinearSolveFunction;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.spa.Nnz;
import ch.alpine.tensor.spa.SparseArray;

public class MatrixAlgebra implements TensorBinaryOperator {
  private final Tensor basis;
  /** m is a matrix with basis elements flattened to rows
   * m has at least as many columns as rows */
  private final Tensor ad;
  private final TensorUnaryOperator solver;

  /** @param basis consisting of n square matrices that generate the Lie algebra
   * @throws Exception if basis contains redundant elements */
  public MatrixAlgebra(Tensor basis) {
    this.basis = basis;
    Tensor matrix = Transpose.of(Tensor.of(basis.stream().map(Flatten::of)));
    Integers.requireEquals(basis.length(), MatrixRank.of(matrix));
    solver = LinearSolveFunction.of(matrix);
    int n = basis.length();
    ad = SparseArray.of(RealScalar.ZERO, n, n, n);
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        Tensor x = toVector(MatrixBracket.of(basis.get(i), basis.get(j))).map(Tolerance.CHOP);
        ad.set(x, Tensor.ALL, j, i);
        ad.set(x.negate(), Tensor.ALL, i, j);
      }
  }

  public int dimensions() {
    return ad.length();
  }

  /** @return sparse array of rank 3 with dimensions n x n x n
   * @see JacobiIdentity */
  public Tensor ad() {
    return ad.unmodifiable();
  }

  /** @param matrix
   * @return vector with vector . basis == matrix
   * @throws Exception if given matrix is not an element in the matrix Lie algebra */
  public Tensor toVector(Tensor matrix) {
    return solver.apply(Flatten.of(matrix));
  }

  /** @param vector
   * @return */
  public Tensor toMatrix(Tensor vector) {
    return vector.dot(basis);
  }

  @Override
  public Tensor apply(Tensor x, Tensor y) {
    return ad.dot(x).dot(y);
  }

  @Override // from Object
  public String toString() {
    return MathematicaFormat.concise("MatrixAlgebra", ad(), Nnz.of((SparseArray) ad));
  }
}
