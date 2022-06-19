// code by jph
package ch.alpine.sophus.lie.td;

import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.mat.IdentityMatrix;

public class TdAdMetric implements TensorMetric {
  private final TdGroupElement stGroupElement;
  private final Tensor B;

  public TdAdMetric(Tensor m) {
    stGroupElement = TdGroup.INSTANCE.element(m).inverse();
    Scalar l = stGroupElement.lambda();
    Scalar t = (Scalar) stGroupElement.t();
    B = Reverse.of(IdentityMatrix.of(2)); // B has to be Ad(m^-1) invariant
    // (2 t)/(1 - \[Lambda])
    B.set(t.add(t).divide(RealScalar.ONE.subtract(l)), 0, 0);
    // System.out.println(Pretty.of(B.map(Round._4)));
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor m, Tensor x) {
    Tensor v = TdGroup.INSTANCE.log(stGroupElement.combine(x));
    return (Scalar) B.dot(v).dot(v);
  }

  public Tensor all(Tensor sequence, Tensor m) {
    return Tensor.of(sequence.stream().map(x -> distance(m, x)));
  }
}
