package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.MinMax;
import ch.alpine.tensor.sca.Clip;

public enum So2BiinvariantMeans implements BiinvariantMean {
  /** Hint:
   * angles are required to lie on a half-circle which is not necessarily centered at the origin
   * 
   * Reference:
   * https://hal.inria.fr/inria-00073318/
   * by Xavier Pennec */
  LINEAR {
    @Override
    public Tensor mean(Tensor sequence, Tensor weights) {
      Scalar a0 = sequence.Get(0);
      Tensor rangeQ = So2BiinvariantMeans.rangeQ(sequence.map(a0::subtract).map(So2.MOD));
      return So2.MOD.apply(a0.subtract(weights.dot(rangeQ)));
    }
  },
  /** Careful:
   * So2FilterBiinvariantMean is not strictly a biinvariant mean, because the
   * computation is not invariant under permutation of input points and weights
   * for sequences of length 3 or greater. */
  FILTER {
    @Override
    public Tensor mean(Tensor sequence, Tensor weights) {
      // sequences of odd and even length are permitted
      int middle = sequence.length() / 2;
      Scalar a0 = sequence.Get(middle);
      return So2.MOD.apply(a0.subtract(weights.dot(sequence.map(a0::subtract).map(So2.MOD))));
    }
  },
  GLOBAL {
    /** biinvariant mean defined globally on SO(2) for arbitrary weights
     * 
     * invariant under simultaneous permutation of control point sequence and weight vector
     * 
     * elements of SO(2) are represented as scalars
     * 
     * Reference:
     * "Illumination for computer generated pictures"
     * by Bui Tuong Phong, 1975 */
    @Override
    public Tensor mean(Tensor sequence, Tensor weights) {
      return ArcTan2D.of(weights.dot(sequence.map(AngleVector::of)));
    }
  };

  /** @param sequence
   * @return given sequence
   * @throws Exception if span of entries exceeds or equals pi */
  @PackageTestAccess
  static Tensor rangeQ(Tensor sequence) {
    Clip clip = sequence.stream() //
        .map(Scalar.class::cast) //
        .collect(MinMax.toClip());
    if (Scalars.lessThan(clip.width(), Pi.VALUE))
      return sequence;
    throw new Throw(sequence);
  }
}
