// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Reverse;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

public class StAdMetric implements TensorMetric {
  private final StGroupElement stGroupElement;
  private final Tensor B;

  public StAdMetric(Tensor m) {
    stGroupElement = StGroup.INSTANCE.element(m).inverse();
    Scalar l = stGroupElement.lambda();
    Scalar t = (Scalar) stGroupElement.t();
    B = Reverse.of(IdentityMatrix.of(2)); // B has to be Ad(m^-1) invariant
    // (2 t)/(1 - \[Lambda])
    B.set(t.add(t).divide(RealScalar.ONE.subtract(l)), 0, 0);
    // System.out.println(Pretty.of(B.map(Round._4)));
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor m, Tensor x) {
    Tensor v = StExponential.INSTANCE.log(stGroupElement.combine(x));
    return B.dot(v).dot(v).Get();
  }

  public Tensor all(Tensor sequence, Tensor m) {
    return Tensor.of(sequence.stream().map(x -> distance(m, x)));
  }
}
