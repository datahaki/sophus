![alpine_877](https://user-images.githubusercontent.com/4012178/116814864-1b1a1580-ab5b-11eb-97e6-1441af4ececa.png)

# ch.alpine.sophus

Library for non-linear geometry computations in Java.

![](https://github.com/datahaki/sophus/actions/workflows/mvn_test.yml/badge.svg)

The library was developed with the following objectives in mind
* trajectory design for autonomous robots
* suitable for use in safety-critical real-time systems
* implementation of theoretical concepts with high level of abstraction

## Manifolds

##### Homogeneous Spaces

* n-dimensional sphere $S^n$
* n-dimensional hyperbolic space $H^n$
* Grassmann manifold `Gr(n,k)`
* n-dimensional projective space `RP^n`
* symmetric positive definite matrices `SPD(n)`
* Stiefel manifold `St(n,k)`

##### Lie Groups

* Euclidean space $R^n$
* Heisenberg group `He(n)`
* Special orthogonal group `SO(n)`
* Special Euclidean groups `SE(2)`, `SE(2)` covering, `SE(3)`

---

## Contributors

Jan Hakenberg, Oliver Brinkmann, Joel Gächter


## Integration

From time to time, a version is deployed and made available for maven integration. Specify `repository` and `dependency` of the library `sophus` in the `pom.xml` file of your maven project:

```xml
<dependencies>
  <!-- other dependencies -->
  <dependency>
    <groupId>ch.alpine</groupId>
    <artifactId>sophus</artifactId>
    <version>0.0.9</version>
  </dependency>
</dependencies>

<repositories>
  <!-- other repositories -->
  <repository>
    <id>sophus-mvn-repo</id>
    <url>https://raw.github.com/datahaki/sophus/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
```

The source code is attached to every release.

The branch `master` always contains the latest features for Java 17, and does not correspond to the most recent deployed version generally.

---

## References

* *Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.* by Vincent Arsigny, Xavier Pennec, Nicholas Ayache
* *Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups* by Xavier Pennec, Vincent Arsigny
* *Lie Groups for 2D and 3D Transformations* by Ethan Eade
* *Manifold-valued subdivision schemes based on geodesic inductive averaging* by Nira Dyn, Nir Sharon
* *Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes* by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun
* *Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics* by Kai Hormann, N. Sukumar 
* *Barycentric Subspace Analysis on Manifolds* by Xavier Pennec
* *A matrix-algebraic algorithm for the Riemannian logarithm on the Stiefel manifold under the canonical metric* by Ralf Zimmermann, 2017, https://arxiv.org/pdf/1604.05054.pdf
