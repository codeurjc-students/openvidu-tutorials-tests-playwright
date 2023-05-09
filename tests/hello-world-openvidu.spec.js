// Importar las librerías 'test', 'expect' y 'chromium' desde Playwright Test
const { test, expect, chromium } = require('@playwright/test');

// Definir un test llamado 'homepage has title and links to intro page'
test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  
  // Iniciar una instancia de Chromium en modo headless con un factor de escala de 1 y un user agent específico.
  const browser = await chromium.launch({ 
    headless: true , 
    deviceScaleFactor: 1, 
    userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', 
    args : ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
  });

  // Crear un nuevo contexto de navegación
  const context = await browser.newContext({
     permissions: ['camera', 'microphone'],
   });

  // Crear una nueva página y navegar a la URL proporcionada
  const page1 = await context.newPage();
  await page1.goto('http://127.0.0.1:8080');

  // Hacer clic en el botón 'submit' y esperar a que aparezca un selector específico
  await page1.click('#join input[type="submit"]');
  await page1.waitForSelector('#session', { visible: true });

  // Crear una segunda página y navegar a la misma URL
  const page2 = await context.newPage();
  await page2.goto('http://127.0.0.1:8080');

  // Hacer clic en el botón 'submit' y esperar a que aparezca un selector específico, esperar 5 segundos
  await page2.click('#join input[type="submit"]');
  await page2.waitForSelector('#session', { visible: true });
  await page2.waitForTimeout(5000); 

  // Tomar una captura de pantalla de la segunda página
  await page2.screenshot({ path: 'pr.png' });

  // Buscar los elementos HTML que contienen los streams de video
  const videoElements = await page2.$$('video');

  // Comprobar que hay exactamente dos elementos encontrados
  expect(videoElements.length).toEqual(2);

  // Cerrar las páginas y el navegador.
  await Promise.all([page1.close(), page2.close()]);
  await browser.close();
});
