const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async ({ page }) => {
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1,
    userAgent:
      'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36',
    args: ['--use-fake-ui-for-media-stream', '--use-fake-device-for-media-stream'],
  });

  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });
  

  page = await context.newPage();
  const page2 = await context.newPage();
  
  
  try {
    await page.goto('http://localhost:8081/');
    let iframeHandle = await page.waitForSelector('iframe.openvidu-iframe');
    let frame = await iframeHandle.contentFrame();

    // Haz clic en el botón "JOIN" dentro del iframe
    await frame.click('input[type="submit"]');
    await frame.waitForSelector('#session', { visible: true });

    await page2.goto('http://localhost:8081/');
    iframeHandle = await page2.waitForSelector('iframe.openvidu-iframe');
    frame = await iframeHandle.contentFrame();

    // Haz clic en el botón "JOIN" dentro del iframe
    await frame.click('input[type="submit"]');
    await frame.waitForSelector('#session', { visible: true });
    await frame.waitForTimeout(5000);

    await page2.screenshot({ path: 'pr.png' });

    // Buscar los elementos HTML que contienen los streams de video
    const videoElements = await frame.$$('video');

    // Comprobar que hay exactamente dos elementos encontrados
    expect(videoElements.length).toEqual(2);

    // Cerrar las páginas y el navegador.
    await Promise.all([page2.close()]);
  } catch (error) {
    // Si hay un error, toma una captura de pantalla
    await page.screenshot({ path: 'error-page1.png' });
    await page2.screenshot({ path: 'error-page2.png' });
    throw error; // Lanzar el error nuevamente para que la prueba falle
  } finally {
    await browser.close();
  }
});
