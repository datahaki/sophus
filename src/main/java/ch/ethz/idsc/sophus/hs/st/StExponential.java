// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "A matrix-algebraic algorithm for the Riemannian logarithm on the Stiefel manifold
 * under the canonical metric"
 * by Ralf Zimmermann, 2017
 * https://arxiv.org/pdf/1604.05054.pdf */
public class StExponential implements Exponential, FlattenLog, Serializable {
  private final Tensor x;

  public StExponential(Tensor x) {
    this.x = x;
  }

  @Override
  public Tensor exp(Tensor v) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Tensor log(Tensor y) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Tensor flattenLog(Tensor y) {
    // TODO Auto-generated method stub
    return null;
  }
}
