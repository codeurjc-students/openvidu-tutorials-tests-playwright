const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
const browser = await chromium.launch({ headless: true , deviceScaleFactor: 1, // especificar el factor de escala de la página
   userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', // especificar el user agent
   args : ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
 });

   const context = await browser.newContext({
     permissions: ['camera', 'microphone'],
   });

   const page1 = await context.newPage();
   await page1.goto('http://127.0.0.1:8080');
   await page1.fill('#userName', 'Page1');
   await page1.click('#join input[type="submit"]');
   await page1.waitForSelector('#session', { visible: true });

   const page2 = await context.newPage();

   await page2.goto('http://127.0.0.1:8080');
   await page2.fill('#userName', 'Page2');
   await page2.click('#join input[type="submit"]');
   await page2.waitForSelector('#buttonScreenShare', { state: 'visible' });
   await page2.click('#buttonScreenShare');
   await page2.waitForTimeout(5000); 

   await page1.screenshot({ path: 'tests/screenshot/page1.png' });
   await page2.screenshot({ path: 'tests/screenshot/page2.png' });
   
   // Buscar los elementos HTML que contienen los streams de video
   const videoElements = await page2.$$('video');

   // Comprobar que hay exactamente dos elementos encontrados
   expect(videoElements.length).toEqual(4);

   // Cerrar las páginas y el navegador.

  await Promise.all([page1.close(), page2.close()]);
  await browser.close();
});
