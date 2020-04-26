// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;

public class So3PointExponential implements Exponential {
  private final Tensor p;

  public So3PointExponential(Tensor p) {
    this.p = p;
  }

  @Override
  public Tensor exp(Tensor v) {
    return p.dot(So3Exponential.INSTANCE.exp(LinearSolve.of(p, v)));
  }

  @Override
  public Tensor log(Tensor g) {
    // TODO Auto-generated method stub
    return null;
  }
}
