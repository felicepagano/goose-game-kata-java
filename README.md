# goose-game-kata-java
Goose game kata implemented in java.

Implemented using a TDD approach I tried to be pure functional avoiding mutability.

In order to compile and run you must have at least a JDK 8+ and maven 3 version installed on your
system.

To run the tests run in your preferred terminal the command `mvn test`.

You can also run the application using maven with the following instruction: 

`mvn exec:java -Dexec.mainClass="it.fpagano.kata.java.goose.Game" -Dexec.args="Pippo Pluto"` 

where in the exec.args you can specify the name of the players.

Remember that you must be in the root project folder in order to run maven.

The entire development was made with the following tools version:

> java version "10.0.2"

> Apache Maven 3.5.4