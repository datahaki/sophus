// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

import ch.alpine.tensor.Tensor;

/** References:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * "Projection matrix"
 * on Wikipedia, 2020 */
public record HsDesign(Manifold vectorLogManifold) implements Serializable {
  public HsDesign {
    Objects.requireNonNull(vectorLogManifold);
  }

  /** @param sequence
   * @param point
   * @return */
  public Stream<Tensor> stream(Tensor sequence, Tensor point) {
    return sequence.stream().map(vectorLogManifold.exponential(point)::vectorLog);
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
