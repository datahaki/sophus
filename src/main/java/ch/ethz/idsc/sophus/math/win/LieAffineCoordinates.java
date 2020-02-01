// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/**  */
public class LieAffineCoordinates implements TensorUnaryOperator {
  private final LieExponential lieExponential;
  private final Tensor mean;
  private final Tensor pinv;
  private final Scalar oon;
  private final LieGroupElement ref;

  public LieAffineCoordinates( //
      LieGroup lieGroup, LieExponential lieExponential, //
      BiinvariantMean biinvariantMean, Tensor points) {
    this.lieExponential = lieExponential;
    oon = RationalScalar.of(1, points.length());
    Tensor weights = ConstantArray.of(oon, points.length());
    mean = biinvariantMean.mean(points, weights);
    ref = lieGroup.element(mean).inverse();
    pinv = PseudoInverse.of(Tensor.of(points.stream().map(ref::combine).map(lieExponential::log)));
  }

  @Override
  public Tensor apply(Tensor x) {
    return lieExponential.log(ref.combine(x)).dot(pinv).map(oon::add);
  }
}
