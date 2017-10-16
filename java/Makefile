SRC0=peuplementBD/src
SRC1=peuplementBD/src/scriptsBD

all: Cdestruction Ccreation Cremplissage Cjava

Cdestruction: $(SRC1)/Destruction.java
	mkdir -p peuplementBD/bin
	javac -d peuplementBD/bin -classpath ojdbc6.jar -sourcepath $(SRC0) $<

Ccreation: $(SRC1)/Creation.java
	mkdir -p peuplementBD/bin
	javac -d peuplementBD/bin -classpath ojdbc6.jar -sourcepath $(SRC0) $<

Cremplissage: $(SRC1)/Remplissage.java
	mkdir -p peuplementBD/bin
	javac -d peuplementBD/bin -classpath ojdbc6.jar -sourcepath $(SRC0) $<
	
Cjava : interface/src/vehlib/Vehlib.java
	mkdir -p interface/bin
	javac -d interface/bin -classpath ojdbc6.jar -sourcepath interface/src interface/src/vehlib/Vehlib.java

# Execution:

creation: Ccreation
	java -classpath peuplementBD/bin:ojdbc6.jar scriptsBD/Creation

destruction: Cdestruction
	java -classpath peuplementBD/bin:ojdbc6.jar scriptsBD/Destruction 

remplissage: Cremplissage
	java -classpath peuplementBD/bin:ojdbc6.jar scriptsBD/Remplissage

vehlib: Cjava
	java -classpath interface/bin:ojdbc6.jar vehlib/Vehlib

clean:
	rm -rf peuplementBD/bin interface/bin
