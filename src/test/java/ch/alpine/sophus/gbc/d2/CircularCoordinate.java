// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Total;

public enum CircularCoordinate implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Total.of(solve(levers)));
  }

  public static Tensor solve(Tensor p) {
    int n = p.length();
    Tensor c = CirclePoints.of(n);
    Tensor rhs = Flatten.of(c);
    Tensor lhs = Array.zeros(2 * n, 2 * n);
    for (int i = 0; i < n; ++i) {
      lhs.set(p.Get(i, 0), 2 * i + 0, 2 * i + 1);
      lhs.set(p.Get(i, 1), 2 * i + 1, 2 * i + 1);
      int j = Math.floorMod(i - 1, n);
      lhs.set(p.Get(j, 0), 2 * i + 0, 2 * i);
      lhs.set(p.Get(j, 1), 2 * i + 1, 2 * i);
    }
    Tensor a = LinearSolve.of(lhs, rhs);
    Tensor A = Array.zeros(n, n);
    int count = -1;
    for (int i = 0; i < n; ++i) {
      int j = Math.floorMod(i - 1, n);
      A.set(a.get(++count), i, j);
      A.set(a.get(++count), i, i);
    }
    return A;
  }
}
