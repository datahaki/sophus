// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.itp.RidgeRegression.Form2;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.math.TensorMapping;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Sqrt;
import junit.framework.TestCase;

/** Reference:
 * "Machine Learning - A Probabilistic Perspective"
 * by Kevin P. Murphy, p. 226 */
/* package */ class RidgeRegression implements Serializable {
  private final HsDesign hsDesign;

  /** @param vectorLogManifold non-null */
  public RidgeRegression(VectorLogManifold vectorLogManifold) {
    hsDesign = new HsDesign(vectorLogManifold);
  }

  /* package */ class Form2 implements Serializable {
    private final Tensor matrix;
    private final Tensor sigma_inverse;

    /** @param sequence of n anchor points
     * @param point */
    /* package */ Form2(Tensor sequence, Tensor point) {
      matrix = hsDesign.matrix(sequence, point);
      Scalar factor = RationalScalar.of(1, sequence.length());
      Tensor sigma = Transpose.of(matrix).dot(matrix).multiply(factor);
      // computation of pseudo inverse only may result in numerical deviation from true symmetric result
      sigma = sigma.add(DiagonalMatrix.of(sigma.length(), RealScalar.of(10)));
      sigma_inverse = Symmetrize.of(PseudoInverse.of(sigma).multiply(factor));
    }

    /** @return design matrix with n rows as log_x(p_i)
     * 
     * @see HsDesign */
    public Tensor matrix() {
      return matrix;
    }

    /** @return matrix that is symmetric positive definite if sequence contains sufficient points and
     * parameterization of tangent space is tight, for example SE(2). Otherwise symmetric positive
     * semidefinite matrix, for example S^d as embedded in R^(d+1).
     * 
     * @see PositiveDefiniteMatrixQ
     * @see PositiveSemidefiniteMatrixQ */
    public Tensor sigma_inverse() {
      return sigma_inverse;
    }

    /** @return diagonal of influence matrix */
    public Tensor leverages() {
      return Tensor.of(matrix.stream() //
          .map(v -> sigma_inverse.dot(v).dot(v)) //
          .map(Scalar.class::cast) //
          // theory guarantees that leverage is in interval [0, 1]
          // so far the numerics did not result in values below 0 here
          .map(Sqrt.FUNCTION));
    }
  }
}

public class RidgeRegressionTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    RidgeRegression ridgeRegression = new RidgeRegression(vectorLogManifold);
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      for (Tensor point : sequence) {
        Form2 form2 = ridgeRegression.new Form2(sequence, point);
        Tensor sigma_inverse = form2.sigma_inverse();
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      }
      {
        Tensor point = RandomVariate.of(distribution, 3);
        ridgeRegression.new Form2(sequence, point).leverages();
        Tensor shift = RandomVariate.of(distribution, 3);
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
          Tensor all = tensorMapping.slash(sequence);
          Tensor one = tensorMapping.apply(point);
          ridgeRegression.new Form2(all, one).leverages();
          // System.out.println(Chop._05.close(l1, l2));
        }
      }
    }
  }
}
