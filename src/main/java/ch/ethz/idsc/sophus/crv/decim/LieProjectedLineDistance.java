// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

public class LieProjectedLineDistance implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final LieGroup lieGroup;
  private final Exponential exponential;

  public LieProjectedLineDistance(LieGroup lieGroup, Exponential exponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.exponential = Objects.requireNonNull(exponential);
  }

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor beg, Tensor end) {
    return new NormImpl(beg, end);
  }

  private class NormImpl implements TensorNorm, Serializable {
    private final LieGroupElement lieBeg;
    private final LieGroupElement lieInv;
    private final Tensor normal;

    public NormImpl(Tensor beg, Tensor end) {
      lieBeg = lieGroup.element(beg);
      lieInv = lieBeg.inverse();
      this.normal = NORMALIZE_UNLESS_ZERO.apply(exponential.log(lieInv.combine(end)));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      Tensor vector = exponential.log(lieInv.combine(tensor));
      Tensor project = vector.dot(normal).pmul(normal);
      Tensor along = lieBeg.combine(exponential.exp(project));
      Tensor dir = lieGroup.element(along).inverse().combine(tensor);
      return Norm._2.ofVector(exponential.log(dir));
    }
  }
}
