package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.Tolerance;

public enum AdBuilder {
  ;
  public static Tensor of(Tensor basis) {
    int n = basis.length();
    Tensor m = Transpose.of(Tensor.of(basis.stream().map(Flatten::of)));
    Tensor ad = Tensors.matrix((i, j) -> solve(m, MatrixBracket.of(basis.get(i), basis.get(j))), n, n);
    return Transpose.of(ad, 2, 1, 0);
  }

  private static Tensor solve(Tensor m, Tensor rhs) {
    Tensor b = Flatten.of(rhs);
    Tensor x = LinearSolve.any(m, b);
    Tolerance.CHOP.requireClose(m.dot(x), b);
    return x;
  }
}
