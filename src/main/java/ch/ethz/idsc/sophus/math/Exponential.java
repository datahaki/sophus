// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;

/** Log[g.m.g^-1] == Ad[g].Log[m] */
public interface Exponential
// extends FlattenLog
{
  // TODO
  /** @param x in the Lie-algebra
   * @return element g of the Lie-group with x == log g, and g == exp x */
  Tensor exp(Tensor x);

  /** @param g element in the Lie group
   * @return element x in the se2 Lie algebra with x == log g, and g == exp x */
  Tensor log(Tensor g);
}
