// code by jph
package showcase;

import org.junit.jupiter.api.function.Executable;

public enum ThrowQ {
  ;
  /** @param executable
   * @return whether executable throws an exception */
  public static boolean of(Executable executable) {
    try {
      executable.execute();
      return false;
    } catch (Throwable e) {
      return true;
    }
  }
}
