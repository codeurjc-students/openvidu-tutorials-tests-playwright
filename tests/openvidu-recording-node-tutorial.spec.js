const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  const browser = await chromium.launch({ 
    headless: true, 
    deviceScaleFactor: 1,
    userAgent: 'Chrome/88.0.4324.182', 
    args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream", "--ignore-certificate-errors"]
  });

  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });

  const page1 = await context.newPage();
  
  const context2 = await browser.newContext({ incognito: true });
  
  const page2 = await context2.newPage();

  try {
    await page1.goto('http://localhost:5000');

    await page1.click('#join-btn');
    await page1.waitForSelector('#session', { visible: true });
    await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });

    await page2.goto('http://localhost:5000');
                      

    await page2.click('#join-btn');
    await page2.waitForSelector('#session', { visible: true });
    await page2.click('#buttonStartRecording');
    await page2.click('#buttonGetRecording');
    await page2.waitForTimeout(5000); 
    await page2.screenshot({ path: '../results/screenshots/page2_startrecording_screenshot.png' });
    await page2.click('#buttonStopRecording');
    await page2.click('#buttonGetRecording');
    await page2.screenshot({ path: '../results/screenshots/page2_stoprecording_screenshot.png' });

    // Buscar los elementos HTML que contienen los streams de video
    const videoElements = await page2.$$('video');

    // Comprobar que hay exactamente dos elementos encontrados
    expect(videoElements.length).toEqual(2);
    
    // Cerrar las páginas y el navegador.
    await Promise.all([page1.close(), page2.close()]);
  
  
  } catch (error) {
    // En caso de error, captura las pantallas y registra el error
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    throw error; // Lanza el error nuevamente para que el test falle
  } 
});
