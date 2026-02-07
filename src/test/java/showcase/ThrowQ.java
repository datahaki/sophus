// code by jph
package showcase;

import org.junit.jupiter.api.function.Executable;

public enum ThrowQ {
  ;
  public static boolean of(Executable executable) {
    try {
      executable.execute();
      return true;
    } catch (Throwable e) {
      // ---
    }
    return false;
  }
}
