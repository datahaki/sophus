// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.MatrixDotTranspose;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;

/** special orthogonal group of n x n orthogonal matrices with determinant 1
 * 
 * SO(n) group of orthogonal matrices with determinant +1
 * 
 * for n == 3 use {@link So3Group}
 * 
 * 2. The Canonical Riemannian Metric (Geodesic Distance)
 * 
 * To stay "on the manifold," we define a metric using the Lie algebra so(n), which consists of
 * n×n skew-symmetric matrices.
 * 
 * The inner product on the tangent space at the identity (the Lie algebra) is given by:
 * ⟨A,B⟩=Tr(ATB)=−Tr(AB)
 * 
 * This induces a Riemannian metric across the whole group. The resulting geodesic distance
 * between two points R1​ and R2​ is the length of the shortest arc
 * connecting them:
 * dg​(R1​,R2​)=|log(R1T​R2​)|F​
 * 
 * Interpretation: For SO(3), this distance corresponds exactly to the angle of rotation
 * required to get from R1​ to R2​.
 * 
 * Property: This metric is bi-invariant, making it the "natural" choice for most physics and robotics applications. */
public class SoGroup extends GlGroup implements MetricManifold {
  public static final SoGroup INSTANCE = new SoGroup();

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.of(this, Chop._10, SoPhongMean.INSTANCE);
  }

  @Override // from MemberQ
  public MemberQ isPointQ() {
    return x -> Tolerance.CHOP.isClose(Det.of(x), RealScalar.ONE) //
        && OrthogonalMatrixQ.INSTANCE.test(x);
  }

  private enum Exponential0 implements Exponential {
    INSTANCE;

    @Override // from Exponential
    public Tensor exp(Tensor matrix) {
      return MatrixExp.of(matrix);
    }

    @Override // from Exponential
    public Tensor log(Tensor matrix) {
      return MatrixLog.of(matrix);
    }

    @Override
    public ZeroDefectArrayQ isTangentQ() {
      return TSoMemberQ.INSTANCE;
    }
  }

  @Override
  public Exponential exponential0() {
    return Exponential0.INSTANCE;
  }

  @Override
  public final Tensor invert(Tensor element) {
    return Transpose.of(element);
  }

  @Override // from LieGroupElement
  public final Tensor adjoint(Tensor matrix, Tensor v) { // v is skew with dimensions 3 x 3
    return MatrixDotTranspose.of(dL(matrix, v), matrix);
  }

  @Override // from LieGroupElement
  public final Tensor dL(Tensor matrix, Tensor v) { // v is skew with dimensions 3 x 3
    return matrix.dot(TSoMemberQ.INSTANCE.require(v)); // consistent with So3Transport
  }

  @Override // from TensorMetric
  public final Scalar distance(Tensor p, Tensor q) {
    // formula taken from chatgpt
    Tensor A = exponential(p).log(q);
    Scalar normSquared = Trace.of(A.dot(A)).multiply(RationalScalar.HALF.negate());
    return Sqrt.FUNCTION.apply(normSquared);
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  @Override
  public String toString() {
    return "SO";
  }
}
