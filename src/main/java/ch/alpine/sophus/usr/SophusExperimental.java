// code by jph
package ch.alpine.sophus.usr;

import java.util.List;
import java.util.stream.Stream;

import ch.alpine.sophus.hs.gr.Grassmannian;
import ch.alpine.sophus.hs.h.Hyperboloid;
import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.sophus.hs.spd.SpdNManifold;
import ch.alpine.sophus.hs.st.StiefelManifold;
import ch.alpine.sophus.lie.gl.GlNGroup;
import ch.alpine.sophus.lie.he.HeNGroup;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.sl.SlNGroup;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.sophus.lie.sp.SpNGroup;
import ch.alpine.sophus.lie.td.TdNGroup;
import ch.alpine.sophus.math.api.Manifold;

public enum SophusExperimental {
  ;
  private static final Manifold[] MANIFOLDS = { //
      new StiefelManifold(3, 1), //
      new StiefelManifold(4, 2), //
      //
      new Grassmannian(5, 2), //
      new Grassmannian(6, 3), //
      //
      new SpdNManifold(2), //
      new SpdNManifold(3), //
      //
      new SpNGroup(2), //
      new SpNGroup(3), //
      //
      new Sphere(2), //
      new Sphere(3), //
      //
      new Hyperboloid(2), //
      new Hyperboloid(3), //
      new Hyperboloid(4), //
      // ---
      new GlNGroup(2), //
      new GlNGroup(3), //
      //
      new SlNGroup(2), //
      new SlNGroup(3), //
      new SlNGroup(4), //
      //
      new RnGroup(3), //
      //
      new TdNGroup(1), //
      new TdNGroup(2), //
      new TdNGroup(3), //
      new TdNGroup(5), //
      //
      new HeNGroup(1), //
      new HeNGroup(2), //
      //
      Se2CoveringGroup.INSTANCE, //
      Se2Group.INSTANCE, //
      //
      new SeNGroup(3), //
      new SeNGroup(4), //
      //
      new SoNGroup(2), //
      new SoNGroup(3), //
      new SoNGroup(4) //
  };

  public static <T extends Manifold> List<T> filter(Class<T> cls) {
    return Stream.of(MANIFOLDS) //
        .filter(cls::isInstance) //
        .map(cls::cast) //
        .toList();
  }
}
