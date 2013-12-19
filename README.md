Random avatar generator
=======================

* [0.1.0](https://github.com/hackrslab/maven-repo/raw/gh-pages/org/hackrslab/random-avatar/0.1.0/random-avatar-0.1.0.jar)
* [0.2.0](https://github.com/hackrslab/maven-repo/raw/gh-pages/org/hackrslab/random-avatar/0.2.0/random-avatar-0.2.0.jar)

### Maven

```xml
<project>
  <dependencies>
    <dependency>
      <groupId>org.hackrslab</groupId>
      <artifactId>random-avatar</artifactId>
      <version>0.2.0</version>
    </dependency>
  </dependencies>

  <repositories>
    <repository>
      <id>hackrslab-repository</id>
      <url>http://hackrslab.github.io/maven-repo</url>
    </repository>
  </repositories>
</project>
```

### SBT

```scala
lazy val defaultSettings = Seq(
  resolvers += "hackrslab-repository" at "http://hackrslab.github.io/maven-repo"
  , libraryDependencies ++= Seq(
    "org.hackrslab" % "random-avatar" % "0.2.0"
  )
)
```

## Sample

![Sample](https://raw.github.com/hackrslab/random-avatar/branch-0.2/samples/random.png)

## Sample code

```java
import org.hackrslab.avatar.RandomAvatar;
import org.hackrslab.avatar.RandomAvatarBuilder;

import java.io.File;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        RandomAvatar generator = new RandomAvatarBuilder()
            .squareSize(400)
            .blockSize(5)
            .asymmetry(false)
            .padding(20)
            .backgroundColor(0xeeeeee)
            .fontColor(0xffffff)
            .addColor(127, 127, 220)
            .addColor(100, 207, 172)
            .addColor(198, 87, 181)
            .addColor(134, 166, 220)
            .build();

        String[] initials = "dgkim84@gmail.com,geekple.com,kdg,kdh,insanehong".split(",");
        for (int i = 0; i < initials.length; i++) {
            generator.generate(new File("samples/avatar"+i+"-initial-1.png"), RandomAvatar.Extra.initial(initials[i]));
            generator.generate(new File("samples/avatar"+i+"-initial-2.png"), RandomAvatar.Extra.initial(initials[i], 2));
            generator.generate(new File("samples/avatar"+i+"-initial-3.png"), RandomAvatar.Extra.initial(initials[i], 3));
            generator.generate(new File("samples/avatar"+i+"-default.png"));
        }
        // OR generator.generate(new XyzOutputStream(), ...);
    }
}
```


