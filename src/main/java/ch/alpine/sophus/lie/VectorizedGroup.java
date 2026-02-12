// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.io.MathematicaFormat;

public class VectorizedGroup implements LieGroup, MetricManifold, VectorEncodingMarker, Serializable {
  // TODO SOPHUS through introspection check for metric or not
  private final LieGroup lieGroup;
  private final MatrixAlgebra matrixAlgebra;
  private final Exponential exponential0;

  public VectorizedGroup(LieGroup lieGroup) {
    this.lieGroup = lieGroup;
    exponential0 = lieGroup.exponential0();
    MatrixGroup matrixGroup = (MatrixGroup) lieGroup;
    matrixAlgebra = new MatrixAlgebra(matrixGroup.matrixBasis());
  }

  @Override
  public Exponential exponential0() {
    return new Exponential() {
      @Override
      public Tensor exp(Tensor v) {
        return exponential0.exp(matrixAlgebra.toMatrix(v));
      }

      @Override
      public Tensor log(Tensor q) {
        return matrixAlgebra.toVector(exponential0.log(q));
      }

      @Override
      public ZeroDefectArrayQ isTangentQ() {
        return VectorQ.INSTANCE;
      }
    };
  }

  @Override
  public Tensor adjoint(Tensor point, Tensor v) {
    return matrixAlgebra.toVector(lieGroup.adjoint(point, matrixAlgebra.toMatrix(v)));
  }

  @Override
  public Tensor dL(Tensor point, Tensor v) {
    return matrixAlgebra.toVector(lieGroup.dL(point, matrixAlgebra.toMatrix(v)));
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return lieGroup.biinvariantMean();
  }

  @Override
  public MemberQ isPointQ() {
    return lieGroup.isPointQ();
  }

  @Override
  public Tensor neutral(Tensor element) {
    return lieGroup.neutral(element);
  }

  @Override
  public Tensor invert(Tensor element) {
    return lieGroup.invert(element);
  }

  @Override
  public Tensor combine(Tensor element1, Tensor element2) {
    return lieGroup.combine(element1, element2);
  }

  @Override
  public int dimensions() {
    return matrixAlgebra.dimensions();
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Vectorized", lieGroup);
  }
}
