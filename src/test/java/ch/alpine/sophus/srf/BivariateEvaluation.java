// code by jph
package ch.alpine.sophus.srf;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.sca.Clip;

/* package */ interface BivariateEvaluation extends BinaryOperator<Scalar> {
  Clip clipX();

  Clip clipY();
}
