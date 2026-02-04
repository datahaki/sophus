package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.PackageTestAccess;

public enum HeBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    HeFormat heFormat = HeFormat.of(weights.dot(sequence));
    Tensor xMean = heFormat.x();
    Tensor yMean = heFormat.y();
    Tensor xyMean = weights.dot(xydot(sequence));
    return heFormat.with( //
        heFormat.z().add(xMean.dot(yMean).subtract(xyMean).multiply(RationalScalar.HALF)));
  }

  @PackageTestAccess
  static Tensor xydot(Tensor sequence) {
    return Tensor.of(sequence.stream() //
        .map(HeFormat::of) //
        .map(xyz -> xyz.x().dot(xyz.y())));
  }
}
