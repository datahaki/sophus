// code by jph
package ch.alpine.sophus.lie.so3;

import org.junit.jupiter.api.RepeatedTest;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.qr.SchurDecomposition;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.sca.Round;

class So3RandomSampleTest {
  @RepeatedTest(10)
  void testSimple() {
    Tensor matrix = RandomSample.of(So3RandomSample.INSTANCE);
    MatrixQ.requireSize(matrix, 3, 3);
    OrthogonalMatrixQ.require(matrix);
    Tolerance.CHOP.requireClose(Det.of(matrix), RealScalar.ONE);
    SchurDecomposition schurDecomposition = SchurDecomposition.of(matrix);
    
    System.out.println(Pretty.of(schurDecomposition.getT().map(Round._3)));
  }
}
