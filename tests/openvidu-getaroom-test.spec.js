const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
const browser = await chromium.launch({ headless: false , deviceScaleFactor: 1, // especificar el factor de escala de la página
   userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', // especificar el user agent
   args : ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
 });

   const context = await browser.newContext({
     permissions: ['camera', 'microphone'],
   });

   const page1 = await context.newPage();
   await page1.goto('http://127.0.0.1:8080');

   // Espera a que el botón esté presente en la página
   await page1.waitForSelector('button.btn-success');

   // Hace clic en el botón con el atributo onclick
   await page1.click('button.btn-success');
   await page1.waitForSelector('#session', { visible: true });
   const inputValue = await page1.$eval('input#copy-input', (input) => input.value);

   const page2 = await context.newPage();

   await page2.goto(inputValue);

   await page2.waitForSelector('#session', { visible: true });
   await page2.waitForTimeout(5000); 
   
   await page2.screenshot({ path: 'pr.png' });
   
   // Buscar los elementos HTML que contienen los streams de video
   const videoElements = await page2.$$('video');

   // Comprobar que hay exactamente dos elementos encontrados
   expect(videoElements.length).toEqual(2);

   // Cerrar las páginas y el navegador.

  await Promise.all([page1.close(), page2.close()]);
  await browser.close();
});
