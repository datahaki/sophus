# ch.ethz.idsc.sophus <a href="https://travis-ci.org/datahaki/sophus"><img src="https://travis-ci.org/datahaki/sophus.svg?branch=master" alt="Build Status"></a>

Library for non-linear geometry computation in Java, version `0.0.1`

![sophus](https://user-images.githubusercontent.com/4012178/76152560-1aa1fe00-60c1-11ea-8318-c25a61b6be64.png)

The library was developed with the following objectives in mind
* trajectory design for autonomous robots
* suitable for use in safety-critical real-time systems
* implementation of theoretical concepts with high level of abstraction

<table>
<tr>
<td>

![curve_se2](https://user-images.githubusercontent.com/4012178/47631757-8f693d80-db47-11e8-9c00-7796b07c48fc.png)

Curve Subdivision

<td>

![smoothing](https://user-images.githubusercontent.com/4012178/47631759-91cb9780-db47-11e8-9dc7-a2631a144ecc.png)

Smoothing

<td>

![wachspress](https://user-images.githubusercontent.com/4012178/62423041-7c7a2f80-b6bc-11e9-874e-414ae13be3ab.png)

Wachspress

<td>

![dubinspathcurvature](https://user-images.githubusercontent.com/4012178/50681318-5d72cc80-100b-11e9-943e-e168d0463eca.png)

Dubins path curvature

</tr>
</table>

## Features

* geodesics in Lie-groups and homogeneous spaces: Euclidean space `R^n`, special Euclidean group `SE(2)`, hyperbolic half-plane `H2`, n-dimensional sphere `S^n`, ...
* parametric curves defined by control points in non-linear spaces: `GeodesicBSplineFunction`, ...
* non-linear smoothing of noisy localization data `GeodesicCenterFilter`
* Dubins path

### Geodesic DeBoor Algorithm

![loops5](https://user-images.githubusercontent.com/4012178/51076078-3c0d8280-1694-11e9-9857-2166598c09b2.png)

B-Spline curves in `SE(2)` produced by DeBoor Algorithm or curve subdivision produce curves in the planar subspace `R^2` with appealing curvature.

### Smoothing using Geodesic Averages

![smoothing](https://user-images.githubusercontent.com/4012178/51090026-283a4d00-1776-11e9-81d3-aae3e34402f1.png)

The sequence of localization estimates of a mobile robot often contains noise.
Instead of using a complicated extended Kalman filter, geodesic averages based on conventional window functions denoise the uniformly sampled signal of poses in `SE(2)`.

### Curve Decimation in Lie Groups

![curve_decimation](https://user-images.githubusercontent.com/4012178/64847671-cf29fe00-d60f-11e9-8993-9f5549388ceb.png)

The pose of mobile robots is typically recorded at high frequencies.
The trajectory can be faithfully reconstructed from a fraction of the samples.

### Biinvariant Generalized Barycentric Coordinates

* for Lie groups: R^d, SE(2), SE(3), ...
* for homogeneous spaces: S^n, Spd

## Contributors

Jan Hakenberg, Oliver Brinkmann, Joel Gächter

## Publications

##### 2018

* *Curve Subdivision in SE(2)*
by Jan Hakenberg,
[viXra:1807.0463](https://vixra.org/abs/1807.0463),
[video](https://www.youtube.com/watch?v=2vDciaUgL4E)
* *Smoothing using Geodesic Averages*
by Jan Hakenberg,
[viXra:1810.0283](https://vixra.org/abs/1810.0283),
[video](https://www.youtube.com/watch?v=dmFO72Pigb4)

##### 2019

* *Curve Decimation in SE(2) and SE(3)*
by Jan Hakenberg,
[viXra:1909.0174](https://vixra.org/abs/1909.0174)

##### 2020

* *Inverse Distance Coordinates for Scattered Sets of Points*
by Jan Hakenberg,
[viXra:2002.0129](https://vixra.org/abs/2002.0129)
* *Biinvariant Generalized Barycentric Coordinates on Lie Groups*
by Jan Hakenberg,
[viXra:2002.0129](https://vixra.org/abs/2002.0584),
[video](https://www.youtube.com/watch?v=n0-YJE15m90)

## Integration

Specify `repository` and `dependency` of the tensor library in the `pom.xml` file of your maven project:

```xml
<repositories>
  <repository>
    <id>tensor-mvn-repo</id>
    <url>https://raw.github.com/datahaki/sophus/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>ch.ethz.idsc</groupId>
    <artifactId>sophus</artifactId>
    <version>0.0.1</version>
  </dependency>
</dependencies>
```

The source code is attached to every release.

---

## References

* *Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.* by Vincent Arsigny, Xavier Pennec, Nicholas Ayache
* *Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups* by Xavier Pennec, Vincent Arsigny
* *Lie Groups for 2D and 3D Transformations* by Ethan Eade
* *Manifold-valued subdivision schemes based on geodesic inductive averaging* by Nira Dyn, Nir Sharon
* *Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes* by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun
* *Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics* by Kai Hormann, N. Sukumar 
* *Barycentric Subspace Analysis on Manifolds* by Xavier Pennec

---

![ethz300](https://user-images.githubusercontent.com/4012178/45925071-bf9d3b00-bf0e-11e8-9d92-e30650fd6bf6.png)
