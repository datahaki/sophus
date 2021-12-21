// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class DistanceMatrixTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Quantity.of(2, "A")));
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    Tensor tensor = DistanceMatrix.of(sequence, Vector2Norm::between);
    SquareMatrixQ.require(tensor);
    Unit unit = Unprotect.getUnitUnique(tensor);
    assertEquals(unit, Unit.of("A"));
    SymmetricMatrixQ.require(tensor);
  }
}
