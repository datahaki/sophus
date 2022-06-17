// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;

/** https://en.wikipedia.org/wiki/Metric_(mathematics)
 * 
 * distance operator that gives distance between two points p and q
 * that is invariant under all symmetries, or null if operator does not exist */
@FunctionalInterface
public interface TensorMetric extends Metric<Tensor> {
  // ---
}
