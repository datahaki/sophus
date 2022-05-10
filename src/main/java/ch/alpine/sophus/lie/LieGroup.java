// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.tensor.Tensor;

/** interface maps tensor coordinate to an element of a lie group */
// TODO SOPHUS API should give LieAlgebra with ad (since this is implied from basis/group action)
public interface LieGroup extends HomogeneousSpace {
  /** function produces an instance of a lie group element from a given tensor
   * 
   * @param tensor
   * @return lie group element */
  LieGroupElement element(Tensor tensor);

  /** @return exponential at neutral element */
  Exponential exponential();

  @Override // from VectorLogManifold
  public default TangentSpace logAt(Tensor point) {
    return exponential(point);
  }

  @Override // from HsManifold
  public default Exponential exponential(Tensor point) {
    return new LieExponential(this, point);
  }

  @Override // from HsManifold
  public default HsTransport hsTransport() {
    return LieTransport.INSTANCE;
  }
}
