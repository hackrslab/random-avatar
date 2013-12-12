Random avatar generator
=======================

[Download](https://raw.github.com/hackrslab/random-avatar/master/dist/random-avatar-0.1.0.jar)

### Maven

```xml
<project>
  <dependencies>
    <dependency>
      <groupId>org.hackrslab</groupId>
      <artifactId>random-avatar</artifactId>
      <version>0.1.0</version>
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
    "org.hackrslab" % "random-avatar" % "0.1.0"
  )
)
```

## Sample

![Sample](https://raw.github.com/hackrslab/random-avatar/branch-0.1.x/samples/random.png)

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
            .addColor(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            .addColor(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            .addColor(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            .build();

        generator.generate(new File("samples/avatar1.png"));
        generator.generate(new File("samples/avatar2.png"));
        generator.generate(new File("samples/avatar3.png"));
        // OR generator.generate(new XyzOutputStream());
    }
}
```


