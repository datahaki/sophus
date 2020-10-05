// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

import ch.ethz.idsc.tensor.Tensor;

/** References:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * "Projection matrix"
 * on Wikipedia, 2020 */
public final class HsDesign implements Serializable {
  private static final long serialVersionUID = -6051895491677912068L;
  // ---
  private final VectorLogManifold vectorLogManifold;

  /** @param vectorLogManifold non-null */
  public HsDesign(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
  }

  /** @param sequence
   * @param point
   * @return */
  public Stream<Tensor> stream(Tensor sequence, Tensor point) {
    return sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog);
  }

  /** In statistics the matrix is called "design matrix"
   * 
   * @param sequence
   * @param point
   * @return */
  public Tensor matrix(Tensor sequence, Tensor point) {
    return Tensor.of(stream(sequence, point));
  }
}
