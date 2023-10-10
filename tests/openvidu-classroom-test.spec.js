const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  const browser = await chromium.launch({ 
    headless: false, 
    deviceScaleFactor: 1,
    userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', 
    args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream", "--ignore-certificate-errors"]
  });

  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });

  const page1 = await context.newPage();
  const page2 = await context.newPage();

  try {
    await page1.goto('https://localhost:5000');

    await page1.click('#join-btn');
    await page1.waitForSelector('#session', { visible: true });

    await page2.goto('https://localhost:5000');
    //await page2.fill('#sessionId', 'SessionS');
    await page2.click('#join-btn');
    await page2.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000); 
   

    await page1.screenshot({ path: 'page1.png' });
    await page2.screenshot({ path: 'page2.png' });
    // Buscar los elementos HTML que contienen los streams de video
    const videoElements = await page2.$$('video');

    // Comprobar que hay exactamente dos elementos encontrados
    expect(videoElements.length).toEqual(2);

  } catch (error) {
    // En caso de error, captura las pantallas y registra el error
    await page1.screenshot({ path: 'page1_error.png' });
    await page2.screenshot({ path: 'page2_error.png' });
    throw error; // Lanza el error nuevamente para que el test falle
  } finally {
    // Cerrar las páginas y el navegador.
    await Promise.all([page1.close(), page2.close()]);
    await browser.close();
  }
});