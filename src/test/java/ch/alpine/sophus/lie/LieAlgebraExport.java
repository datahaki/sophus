// code by jph
package ch.alpine.sophus.lie;

import java.io.File;
import java.io.IOException;

import ch.alpine.sophus.lie.sl.SlAlgebra;
import ch.alpine.sophus.lie.so.SoAlgebra;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Export;

/* package */ enum LieAlgebraExport {
  ;
  public static void main(String[] args) throws IOException {
    File dir = HomeDirectory.file("Projects", "tensor", "src", "test", "resources", "lie");
    // ---
    Export.object(new File(dir, "so4_ad.sparse"), SoAlgebra.of(4).ad());
    Export.object(new File(dir, "so5_ad.sparse"), SoAlgebra.of(5).ad());
    Export.object(new File(dir, "sl3_ad.sparse"), SlAlgebra.of(3).ad());
  }
}
