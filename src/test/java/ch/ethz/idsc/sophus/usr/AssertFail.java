// code by jph
package ch.ethz.idsc.sophus.usr;

import java.util.function.Supplier;

import junit.framework.Assert;

public enum AssertFail {
  ;
  public static void of(Supplier<?> supplier) {
    try {
      supplier.get();
      Assert.fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
