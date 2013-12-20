Random avatar generator
=======================

## Sample

**Fixed Random Image** : http://geekple.com:18191/?seed=dgkim84@gmail.com&initial=KDG

![Sample](http://geekple.com:18191/?seed=dgkim84@gmail.com&initial=K&v=0.2.1)
![Sample](http://geekple.com:18191/?seed=dgkim84@gmail.com&initial=DG&v=0.2.1)
![Sample](http://geekple.com:18191/?seed=dgkim84@gmail.com&initial=KDG&v=0.2.1)
![Sample](http://geekple.com:18191/?seed=admin@geekple.com&initial=K&v=0.2.1)
![Sample](http://geekple.com:18191/?seed=admin@geekple.com&initial=DG&v=0.2.1)
![Sample](http://geekple.com:18191/?seed=admin@geekple.com&initial=KDG&v=0.2.1)

**Random Image** : http://geekple.com:18191/

![Sample](http://geekple.com:18191/?v=0.2.1&i=1)
![Sample](http://geekple.com:18191/?v=0.2.1&i=2)
![Sample](http://geekple.com:18191/?v=0.2.1&i=3)

seed-1.png 과 seed-2.png 는 블럭의 색상과 블럭의 모양이 동일합니다. 물론 글자가 포함되는 경우에는 글자의 백그라운드는 채워집니다.

![Sample](https://raw.github.com/hackrslab/random-avatar/0.2.1/samples/random.png)

## ChangeLogs

* 0.2.1
 * seed를 지정하면 항상 같은 아바타가 나올 수 있도록 제공 - https://raw.github.com/hackrslab/random-avatar/0.2.1/samples/random.png
* 0.2.0
 * 랜덤 아바타 기능 + 3~4개의 영문자를 함께 생성할 수 있도록 기능 제공 - https://raw.github.com/hackrslab/random-avatar/0.2.0/samples/random.png
* 0.1.0
 * 단순한 랜덤 아바타 기능 제공 - https://raw.github.com/hackrslab/random-avatar/0.1.x/samples/random.png

## Downloads

이 라이브러리는 의존하는 라이브러리가 없습니다.

* [0.1.0](https://github.com/hackrslab/maven-repo/raw/gh-pages/org/hackrslab/random-avatar/0.1.0/random-avatar-0.1.0.jar)
* [0.2.0](https://github.com/hackrslab/maven-repo/raw/gh-pages/org/hackrslab/random-avatar/0.2.0/random-avatar-0.2.0.jar)
* [0.2.1](https://github.com/hackrslab/maven-repo/raw/gh-pages/org/hackrslab/random-avatar/0.2.1/random-avatar-0.2.1.jar)

### Maven

```xml
<project>
  <dependencies>
    <dependency>
      <groupId>org.hackrslab</groupId>
      <artifactId>random-avatar</artifactId>
      <version>0.2.1</version>
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
    "org.hackrslab" % "random-avatar" % "0.2.1"
  )
)
```

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

        String[] initials = "dgkim84@gmail.com,admin@geekple.com,dgkim84@daum.net".split(",");
        for (int i = 0; i < initials.length; i++) {
            generator.generate(new File("samples/avatar"+i+"-default.png"));
            generator.generate(new File("samples/avatar"+i+"-initial-1.png"), RandomAvatar.Extra.initial(initials[i]));
            generator.generate(new File("samples/avatar"+i+"-initial-3.png"), RandomAvatar.Extra.initial(initials[i], 3));
            generator.generate(new File("samples/avatar"+i+"-seed-1.png"), RandomAvatar.Extra.seed(initials[i]));
            generator.generate(new File("samples/avatar"+i+"-seed-2.png"), RandomAvatar.Extra.seed(initials[i], 3));
        }
        // OR generator.generate(new XyzOutputStream(), ...);
    }
}
```


