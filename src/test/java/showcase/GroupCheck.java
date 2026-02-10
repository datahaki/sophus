// code by jph
package showcase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.stream.IntStream;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.BchBinaryAverage;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.sophus.lie.VectorizedGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.spa.SparseArray;

public enum GroupCheck {
  ;
  private static final Distribution DISTRIBUTION = TriangularDistribution.of(-0.1, 0, 0.1);

  public static void check(LieGroup lieGroup, RandomSampleInterface randomSampleInterface) {
    Tensor x = RandomSample.of(randomSampleInterface);
    Tensor y = RandomSample.of(randomSampleInterface);
    checkElem(lieGroup, x, y);
  }

  public static void check(LieGroup lieGroup) {
    assertFalse(lieGroup.toString().contains("@"));
    // IO.println(lieGroup);
    assertDoesNotThrow(() -> Serialization.copy(lieGroup));
    if (lieGroup instanceof RandomSampleInterface randomSampleInterface) {
      check(lieGroup, randomSampleInterface);
    }
    if (lieGroup instanceof VectorEncodingMarker vectorEncodingMarker) {
      int dim = vectorEncodingMarker.dimensions();
      Tensor x = RandomVariate.of(DISTRIBUTION, dim);
      Tensor y = RandomVariate.of(DISTRIBUTION, dim);
      Exponential exponential0 = lieGroup.exponential0();
      Tensor exp_x = exponential0.exp(x);
      Tensor exp_y = exponential0.exp(y);
      checkElem(lieGroup, exp_x, exp_y);
      Tolerance.CHOP.requireClose(x, exponential0.log(exp_x));
      Tolerance.CHOP.requireAllZero(exponential0.log(lieGroup.neutral(exp_x)));
      // ---
      checkAd(lieGroup, exp_x, y);
      // ---
      Tensor sequence = Tensor.of(IntStream.range(0, dim + 3) //
          .mapToObj(_ -> RandomVariate.of(DISTRIBUTION, dim)) //
          .map(exponential0::exp));
      int n = sequence.length();
      Tensor weights = ConstantArray.of(RealScalar.of(1.0 / n), n);
      Tensor mean = lieGroup.biinvariantMean().mean(sequence, weights);
      Tensor centered = Tensor.of(sequence.stream().map(lieGroup.actionL(lieGroup.invert(mean))));
      MeanDefect meanDefect = MeanDefect.of(centered, weights, exponential0);
      Tolerance.CHOP.requireAllZero(meanDefect.tangent());
      return;
    }
    if (lieGroup instanceof MatrixGroup matrixGroup) {
      check(new VectorizedGroup(lieGroup));
      // ---
      Tensor matrixBasis = matrixGroup.matrixBasis();
      MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
      Tensor ad = matrixAlgebra.ad();
      TensorBinaryOperator bch = BakerCampbellHausdorff.of(ad, 0xA);
      Exponential exponential0 = lieGroup.exponential0();
      assertEquals(matrixBasis.length(), matrixAlgebra.dimensions());
      assertInstanceOf(SparseArray.class, ad);
      Tensor form = KillingForm.of(ad);
      assertInstanceOf(SparseArray.class, form);
      int dim = matrixAlgebra.dimensions();
      for (Tensor mat : matrixBasis) {
        Tensor exp = exponential0.exp(mat);
        Tensor log = exponential0.log(exp);
        Tolerance.CHOP.requireClose(mat, log);
      }
      Tensor sequence = Tensor.of(IntStream.range(0, dim + 3) //
          .mapToObj(_ -> RandomVariate.of(DISTRIBUTION, dim)) //
          .map(matrixAlgebra::toMatrix) //
          .map(exponential0::exp));
      int n = sequence.length();
      Tensor weights = ConstantArray.of(RealScalar.of(1.0 / n), n);
      Tensor mean = lieGroup.biinvariantMean().mean(sequence, weights);
      Tensor centered = Tensor.of(sequence.stream().map(lieGroup.actionL(lieGroup.invert(mean))));
      MeanDefect meanDefect = MeanDefect.of(centered, weights, exponential0);
      Tolerance.CHOP.requireAllZero(meanDefect.tangent());
      // ---
      Tensor x = RandomVariate.of(DISTRIBUTION, dim);
      Tensor y = RandomVariate.of(DISTRIBUTION, dim);
      Tensor z = bch.apply(x, y);
      Tensor X = exponential0.exp(matrixAlgebra.toMatrix(x));
      Tensor Y = exponential0.exp(matrixAlgebra.toMatrix(y));
      Tensor v = matrixAlgebra.toVector(exponential0.log(lieGroup.combine(X, Y)));
      Tolerance.CHOP.requireClose(z, v);
      Scalar lambda = RandomVariate.of(UniformDistribution.unit(20));
      Tensor binavg = new BchBinaryAverage(bch).split(x, y, lambda);
      Tensor compar = matrixAlgebra.toVector(exponential0.log(lieGroup.split(X, Y, lambda)));
      Tolerance.CHOP.requireClose(binavg, compar);
      return;
    }
    throw new Throw(lieGroup);
  }

  private static void checkElem(LieGroup lieGroup, Tensor X, Tensor Y) {
    Tensor Z = lieGroup.combine(X, Y);
    Tensor iX = lieGroup.invert(X);
    Tensor iY = lieGroup.invert(Y);
    Tolerance.CHOP.requireClose(lieGroup.combine(X, iX), lieGroup.neutral(X));
    Tolerance.CHOP.requireClose(lieGroup.combine(Y, iY), lieGroup.neutral(Y));
    Tolerance.CHOP.requireClose(lieGroup.combine(iX, X), lieGroup.neutral(X));
    Tolerance.CHOP.requireClose(lieGroup.combine(iY, Y), lieGroup.neutral(Y));
    Tolerance.CHOP.requireClose(lieGroup.combine(iY, iX), lieGroup.invert(Z));
  }

  private static void checkAd(LieGroup lieGroup, Tensor x, Tensor Y) {
    Exponential exponential0 = lieGroup.exponential0();
    Tensor adjoint = lieGroup.adjoint(x, Y);
    Tensor y = exponential0.exp(Y);
    Tensor lhs = exponential0.exp(adjoint);
    Tensor rhs = lieGroup.conjugation(x).apply(y);
    Tolerance.CHOP.requireClose(lhs, rhs);
    Tensor log = exponential0.log(rhs);
    Tolerance.CHOP.requireClose(adjoint, log);
  }

  public static void showBasis(LieGroup lieGroup) {
    if (lieGroup instanceof MatrixGroup matrixGroup) {
      Tensor matrixBasis = matrixGroup.matrixBasis();
      for (var elem : matrixBasis)
        IO.println(Pretty.of(elem));
    }
  }
}
