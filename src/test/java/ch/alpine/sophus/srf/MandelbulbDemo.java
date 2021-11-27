// code by jph
package ch.alpine.sophus.srf;

import ch.alpine.sophus.lie.r3.NylanderPower;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/* package */ enum MandelbulbDemo implements BivariateEvaluation {
  INSTANCE;

  private static final int DEPTH = 40;
  private static final int EXPONENT = 8;
  private static final Scalar THRESHOLD = RealScalar.of(5.0);

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Tensor c = Tensors.of(re, im, RealScalar.of(0.505));
    Tensor x = Tensors.vector(0.0, 0.0, 0.0);
    Scalar nrm = null;
    for (int index = 0; index < DEPTH; ++index) {
      x = NylanderPower.of(x.add(c), EXPONENT);
      if (Scalars.lessThan(THRESHOLD, Vector2NormSquared.of(x)))
        return RealScalar.ZERO;
      if (index == 6)
        nrm = Vector2NormSquared.of(x.add(c)); //
    }
    return nrm;
  }

  @Override
  public Clip clipX() {
    return Clips.positive(1.0);
  }

  @Override
  public Clip clipY() {
    return Clips.positive(1.0);
  }

  public static void main(String[] args) throws Exception {
    StaticHelper.export(INSTANCE, NylanderPower.class, ColorDataGradients.CLASSIC);
  }
}
