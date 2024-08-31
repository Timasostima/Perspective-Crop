# Perspective-Crop
Perspective-Crop allows you to apply a perspective crop effect by selecting four points on an image and transforming its perspective using OpenCV and JavaFX.

## Prerequisites
- JDK 11+
- OpenCV
- JavaFX
- EclipseIDE

## Installation
- Clone the Repo:
```
  git clone https://github.com/Timasostima/Perspective-Crop.git
```
- Add Libraries:
In Eclipse, go to Project > Properties > Java Build Path > Libraries, and add the OpenCV and JavaFX JAR files.

## VM Arguments:
Add the following to Run Configurations > VM Arguments:
```
  --module-path "javafx lib path" --add-modules javafx.controls,javafx.fxml
```
