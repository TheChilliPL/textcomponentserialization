# TextComponent serialization
TextComponent serializer library for Spigot!

## How to include
### Repository
You need to add the JitPack repository.

<details><summary>Gradle</summary>

  ```gradle
  repositories {
    // ...
    maven { url 'https://jitpack.io' }
  }
  ```
</details>
<details><summary>Maven</summary>

  ```xml
  <repositories>
    <!-- ... -->
    <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
  </repositories>
  ```
</details>

### Dependency
Then add the dependency to the project.

<details><summary>Gradle</summary>

  ```gradle
  dependencies {
    // ...
    implementation 'me.patrykanuszczyk:textcomponentserialization:VERSION'
  }
  ```
</details>
<details><summary>Maven</summary>

  ```xml
  <dependencies>
    <!-- ... -->
    <dependency>
      <groupId>me.patrykanuszczyk</groupId>
      <artifactId>textcomponentserialization</artifactId>
      <version>VERSION</version>
    </dependency>
  </dependencies>
  ```
</details>

`VERSION` must be replaced with a valid version tag, e.g. `1.0`, or, if you wish to use an unpublished version,
using short commit hash or `branch-SNAPSHOT`.  
For more information, check out [JitPack documentation](https://jitpack.io/docs/#building-with-jitpack).

### Shading bStats
Shading copies all the necessary classes to your plugin, when building it, and relocates the library to another package to
avoid conflicts between versions, etc.  
You can do it, using shading plugins for maven or gradle.

<details><summary>Gradle</summary>

  ```gradle
  plugins {
    id 'com.github.johnrengelman.shadow' version '5.2.0'
  }
  
  shadowJar {
    relocate 'me.patrykanuszczyk.textcomponentserialization', 'YOUR.PACKAGE'
  }
  ```
</details>
<details><summary>Maven</summary>

  ```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.0</version>
        <configuration>
          <relocations>
            <relocation>
              <pattern>me.patrykanuszczyk.textcomponentserialization</pattern>
              <shadedPattern>YOUR.PACKAGE</shadedPattern>
            </relocation>
          </relocations>
        </configuration>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  ```
</details>

`YOUR.PACKAGE` must be replaced with the package you want to move the library to when building.
I suggest setting it to a subpackage of your plugin.
