// Importar las librerías necesarias
const { test, expect, chromium } = require('@playwright/test');

// Definir el test a realizar
test('Checking for the presence of two active webcams in an OpenVidu session', async () => {

   // Abrir una instancia del navegador Chromium en modo headless
   const browser = await chromium.launch({ 
      headless: true, 
      deviceScaleFactor: 1, // especificar el factor de escala de la página
      userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', // especificar el user agent
      args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
   });

   // Crear un nuevo contexto de navegación en la instancia del navegador
   const context = await browser.newContext({
      permissions: ['camera', 'microphone'],
   });

   // Crear una nueva página en el contexto de navegación y dirigirse a la página web indicada
   const page1 = await context.newPage();
   await page1.goto('http://127.0.0.1:8080');

   // Hacer clic en el botón de unirse a la sesión y esperar a que aparezca la página de sesión
   await page1.click('#join input[type="submit"]');
   await page1.waitForSelector('#session', { visible: true });

   // Crear una segunda página en el contexto de navegación y dirigirse a la página web indicada
   const page2 = await context.newPage();
   await page2.goto('http://127.0.0.1:8080');

   // Hacer clic en el botón de unirse a la sesión y esperar a que aparezca la página de sesión
   await page2.click('#join input[type="submit"]');
   await page2.waitForSelector('#session', { visible: true });
   
   await page1.screenshot({ path: 'pr1.png' });
   await page2.screenshot({ path: 'pr2.png' });
   
   // Buscar los elementos HTML que contienen los streams de video en la página 1
   const videoElements =  page1.$$('video');

   // Comprobar que hay exactamente dos elementos encontrados
   expect(videoElements.length).toEqual(2);
   
   // Buscar los elementos HTML que contienen los streams de video en la página 2
   videoElements =  page2.$$('video');

   // Comprobar que hay exactamente dos elementos encontrados
   expect(videoElements.length).toEqual(2);

   // Cerrar las páginas y el navegador.
   await Promise.all([page1.close(), page2.close()]);
   await browser.close();
});
