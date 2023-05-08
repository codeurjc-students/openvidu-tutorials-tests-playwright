const { test, expect, chromium } = require('@playwright/test');

test('homepage has title and links to intro page', async () => {
  const browser = await chromium.launch({ headless: true , deviceScaleFactor: 1, // especificar el factor de escala de la página
  userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', // especificar el user agent
  args : ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
});

  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });
  
  const page1 = await context.newPage();
  await page1.goto('http://127.0.0.1:8080');
  
  await page1.click('#join input[type="submit"]');
  await page1.waitForSelector('#session', { visible: true });
 
  const page2 = await context.newPage();

  await page2.goto('http://127.0.0.1:8080');
  //await page2.fill('#sessionId', 'SessionS');
  await page2.click('#join input[type="submit"]');
  await page2.waitForSelector('#session', { visible: true });
  await page2.waitForTimeout(5000); 
  
 

  // Capturar imágenes de video en ambas páginas.
  const [screenshot1, screenshot2] = await Promise.all([
        await page1.screenshot({ type: 'jpeg', });
        await page2.screenshot({ type: 'jpeg', });
  ]);
  // Comparar las imágenes de video.
  expect(screenshot1).toEqual(screenshot2);

  // Cerrar las páginas y el navegador.
  await Promise.all([page1.close(), page2.close()]);
  await browser.close();

});
