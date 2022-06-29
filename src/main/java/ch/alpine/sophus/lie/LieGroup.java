// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;

/** interface maps tensor coordinate to an element of a lie group
 * 
 * exponential at neutral element */
// TODO SOPHUS API should give LieAlgebra with ad (since this is implied from basis/group action)
// .. and vectorLog implies basis
public interface LieGroup extends HomogeneousSpace, Exponential {
  /** function produces an instance of a lie group element from a given tensor
   * 
   * @param tensor
   * @return lie group element */
  LieGroupElement element(Tensor tensor);

  @Override // from Manifold
  public default Exponential exponential(Tensor point) {
    return new LieExponential(this, point);
  }

  @Override // from HomogeneousSpace
  public default HsTransport hsTransport() {
    return LieTransport.INSTANCE;
  }
}
