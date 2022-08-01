// code by stegu
package ch.alpine.sophus.math.noise;

/** class extracted from {@link SimplexContinuousNoise} */
/* package */ class Grad {
  private final double x;
  private final double y;
  private final double z;
  private final double w;

  Grad(double x, double y, double z) {
    this(x, y, z, 0.0);
  }

  Grad(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  double dot(double x, double y) {
    return this.x * x + this.y * y;
  }

  double dot(double x, double y, double z) {
    return this.x * x + this.y * y + this.z * z;
  }

  double dot(double x, double y, double z, double w) {
    return this.x * x + this.y * y + this.z * z + this.w * w;
  }
}
