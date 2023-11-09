# Directorio de Registros de Ejecuciones

Este directorio se utiliza para almacenar registros (logs) de ejecuciones de programas, scripts o tareas en Linux. Los registros son útiles para mantener un seguimiento de las actividades, errores y eventos importantes relacionados con las ejecuciones en el sistema.

## Estructura del Directorio

El directorio se organiza de la siguiente manera:

results/logs
├── ejecucion_1.txt
├── ejecucion_2.txt
├── ...



Cada archivo de registro (`*.txt`) captura la salida estándar y, si es aplicable, la salida de error de una ejecución específica. Los registros se nombran de manera descriptiva para identificar la ejecución a la que pertenecen.

## Uso del Directorio

Para almacenar registros de una ejecución, sigue estos pasos:

1. Crea un nuevo archivo de registro en este directorio, preferiblemente con un nombre descriptivo.
2. Ejecuta el programa, script o tarea que deseas registrar y redirige su salida estándar y/o salida de error a ese archivo de registro.

   Por ejemplo:

   ```bash
   proceso > ejecucion_1.txt 2>&1

