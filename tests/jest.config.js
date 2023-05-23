const HtmlReporter = require('jest-html-reporter');

module.exports = {
  // Otras configuraciones...

  reporters: [
    'default',
    [HtmlReporter, {
      outputPath: './test-report/index.html', // Ruta donde se guardará el informe
      pageTitle: 'Reporte de Pruebas', // Título del informe
      includeFailureMsg: true, // Incluir mensajes de error en el informe
      includeConsoleLog: true, // Incluir registros de consola en el informe
    }],
  ],
};
