// code by jph
package ch.ethz.idsc.sophus.lie.sopq;

import java.io.IOException;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TSopqProjectTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    TSopqProject tSopqProject = new TSopqProject(3, 2);
    Tensor x = RandomVariate.of(NormalDistribution.standard(), 5, 5);
    Tensor sopq = tSopqProject.apply(x);
    Tensor exp = MatrixExp.of(sopq);
    Serialization.copy(new SopqMemberQ(3, 2)).require(exp);
  }
}
