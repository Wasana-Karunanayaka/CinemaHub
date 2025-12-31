$classpath = "out;C:\Users\Waasana\Downloads\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar"
if (-not (Test-Path "out")) { mkdir out }

# Start compilation
Write-Host "Compiling source files..."
javac -d out `
    com/cinemahub/model/*.java `
    com/cinemahub/util/*.java `
    com/cinemahub/dao/*.java `
    com/cinemahub/service/*.java `
    com/cinemahub/ui/*.java

if ($?) {
    Write-Host "Compilation successful. Running CinemaHub..."
    java -cp $classpath com.cinemahub.ui.CinemaHub
}
else {
    Write-Host "Compilation failed."
}
