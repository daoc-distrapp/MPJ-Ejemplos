# MPJ-Ejemplos
Algunos ejemplos del uso de MPJ-Express

# Instalación
Descargar "MPJ Express Software" en: http://mpj-express.org/download.php

Instrucciones detalladas en: http://mpj-express.org/readme.html

# Rápidamente...

## Ejecución multicore en windows

Descomprima el instalador en algún directorio, de preferencia sin espacios en el path. Ej: `C:\Users\yo\mpj`

Vaya a Settings -> About -> Advanced system settings -> Environment Variables -> System variables

Incluya la variable MPJ_HOME con el directorio de instalación: `C:\Users\yo\mpj`

Modifique la variable Path e incluya la ruta al subdirectorio bin: `...;C:\Users\yo\mpj\bin`

Para compilar un programa abra una consola en el directorio donde está el programa y: `javac -cp .;%MPJ_HOME%/lib/mpj.jar Ej1Mpj.java`

Para ejecutar: `mpjrun Ej1Mpj` o `mpjrun -np 8 Ej1Mpj`

## Ejecución clúster en Linux

En todos los equipos descomprima el instalador en algún directorio. Ej: `/home/yo/mpj/`

Edite el archivo .bashrc: `nano ~/.bashrc` e incluya *al inicio*:
```
export MPJ_HOME="/home/yo/mpj"
export PATH="$PATH:$MPJ_HOME/bin"
```
Grabe con `Ctrl+o` y cierre con `Ctrl+x`. Cierre la consola y vuelva a abrirla para actualizar.

Si puede ejecutar `mpjrun.sh` y ve información de ayuda específica de MPJ entonces está Ok.

A continuación la guía asume que tiene al menos un par de equipos, físicos o virtuales, Ubuntu 20+ server interconectados (pueden al menos hacer ping entre ellos) y sin restricciones de firewalls (ej: sudo ufw status --> Status: inactive). Adicionalmente en todos los equipos debe tener un usuario con el mismo nombre, con el que va a estar conectado y trabajar (puede llamarse 'mpi', 'pedro', ..., por ejemplo).

Es necesario que todos los equipos del clúster conozcan las IPs de los otros. Regístrelas en el /etc/hosts de todos los equipos.

Es preferible que todos los equipos puedan conectarse entre ellos por ssh, sin poner clave de usuario. Revise: https://github.com/daoc/Instrucciones/blob/master/Ubuntu.md#conectarse-por-ssh-sin-password, y luego verifique que puede conectarse de una máquina a la otra, poniendo solo el nombre de la máquina (no el usuario ni la IP): >`ssh equipo1` ó >`ssh equipo2`.

Puede ser útil tener un directorio compartido por todos los equipos (samba, nfs, ...), al cual se acceda en el mismo path. Ej: `\home\yo\mpjdata`

En dicho directorio incluirá los programas y datos que usará, así como un archivo donde incluirá los nombres de todos los equipos en su clúster; este archivo suele llamarse "machines", y tendrá un nombre por línea. Ej:
```
equipo1
equipo2
equipo3
```

Ubicado en dicho directorio, ejecute en una máquina, que será el master: `mpjboot machines`. Debe ver mensajes, uno por cada equipo en el clúster, indicando que el "...Daemon started successfully..."

Desde el mismo master ejecute su programa: `mpjrun.sh -np 9 -dev niodev Ej1Mpj`

Para detener el clúster, ejecute desde el master: `mpjhalt machines`

## Plugin para Eclipse (o STS4)

Facilita el desarrollo y prueba de los programas.

Instrucciones completas en: http://mpj-express.org/docs/guides/RunningandDebuggingMPJExpresswithEclipse.pdf

Lista de los métodos en MPJ-Express: http://mpj-express.org/docs/guides/listofmpjmethods.pdf
