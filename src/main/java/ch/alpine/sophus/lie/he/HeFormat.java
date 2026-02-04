// code by jph
package ch.alpine.sophus.lie.he;

import java.io.Serializable;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Integers;

/** class used for group elements as well as algebra elements */
/* package */ record HeFormat(Tensor x, Tensor y, Scalar z) implements Serializable {
  /** @param xyz vector of length 2 * n + 1
   * @return */
  public static HeFormat of(Tensor xyz) {
    int n2 = Integers.requireOdd(xyz.length()) - 1;
    int n = n2 / 2;
    return new HeFormat( //
        VectorQ.require(xyz.extract(0, n)), //
        VectorQ.require(xyz.extract(n, n2)), //
        Last.of(xyz));
  }

  public Tensor with(Scalar a) {
    return Join.of(x, y).append(a);
  }

  public Tensor toCoordinate() {
    return with(z);
  }

  public HeFormat combine(HeFormat heFormat) {
    return new HeFormat( //
        x.add(heFormat.x), //
        y.add(heFormat.y), //
        z.add(heFormat.z).add(x.dot(heFormat.y())));
  }

  public HeFormat inverse() {
    return new HeFormat(x.negate(), y.negate(), (Scalar) x.dot(y).subtract(z));
  }

  public HeFormat exp() {
    return new HeFormat(x, y, z.add(x.dot(y).multiply(RationalScalar.HALF)));
  }

  public HeFormat log() {
    return new HeFormat(x, y, z.subtract(x.dot(y).multiply(RationalScalar.HALF)));
  }
}
