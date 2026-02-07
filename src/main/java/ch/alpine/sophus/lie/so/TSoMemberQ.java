// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

public enum TSoMemberQ {
  ;
  public static final ZeroDefectArrayQ INSTANCE = new AntisymmetricMatrixQ(Chop._08);
}
