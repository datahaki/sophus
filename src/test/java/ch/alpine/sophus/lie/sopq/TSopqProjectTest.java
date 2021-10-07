// code by jph
package ch.alpine.sophus.lie.sopq;

import java.io.IOException;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
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
