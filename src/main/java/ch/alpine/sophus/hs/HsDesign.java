// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.stream.Stream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;

/** interface provides capability to generate design matrices from
 * collection of points and base point
 * 
 * References:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * "Projection matrix"
 * on Wikipedia, 2020 */
public record HsDesign(Manifold manifold) implements Serializable {
  /** @param sequence
   * @param point
   * @return */
  public Stream<Tensor> stream(Tensor sequence, Tensor point) {
    return sequence.stream() //
        .map(manifold.exponential(point)::log) //
        .map(VectorQ::require);
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
