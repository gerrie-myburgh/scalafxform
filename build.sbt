val scala3Version = "3.0.0-RC3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scalafxforms",
    version := "0.0.0",

    scalaVersion := scala3Version,
// https://mvnrepository.com/artifact/org.scalafx/scalafx
//libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24",

    libraryDependencies += "javax.validation" % "validation-api" % "2.0.1.Final",
    libraryDependencies += "org.hibernate.validator" % "hibernate-validator" % "7.0.1.Final",
    libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.2",
    libraryDependencies += "org.json" % "json" % "20210307",
    libraryDependencies += "com.miglayout" % "miglayout-javafx" % "11.0",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.0-alpha1",
    libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "2.0.0-alpha1",
 
  )

  unmanagedBase := baseDirectory.value / "lib"

  // Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "16" classifier osName
)