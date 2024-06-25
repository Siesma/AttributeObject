cd src
javac -d ../build *.java io/*.java attr/*.java
cd ../build
jar cf libAttrObject.jar .