// code by jph
package ch.alpine.sophus.math.noise;

/* package */ enum StaticHelper {
  ;
  /** method is faster than (int)Math.floor(x)
   * 
   * @param x
   * @return */
  public static int floor(double x) {
    int xi = (int) x;
    return x < xi ? xi - 1 : xi;
  }
}
