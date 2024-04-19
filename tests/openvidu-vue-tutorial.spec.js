const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Inicia una instancia del navegador Chromium en modo headless con configuraciones específicas.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1,
    userAgent: 'Chrome/88.0.4324.182',
    args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
  });

  // Crea un nuevo contexto en el navegador con los permisos necesarios.
  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });

  // Crea dos nuevas páginas dentro del contexto del navegador.
  const page1 = await context.newPage();
  const page2 = await context.newPage();

  try {
    // Navega a una URL específica en la primera página y realiza acciones como llenar un campo de texto y hacer clic en un botón.
    await page1.goto('http://localhost:8080');
    await page1.fill('input[type="text"]', 'User1');
    await page1.click('button.btn.btn-lg.btn-success');
    // Espera a que un elemento específico se vuelva visible.
    await page1.waitForSelector('#session', { visible: true });
    await page1.waitForTimeout(5000);

    // Encuentra los elementos de video en la página y verifica que haya exactamente dos de ellos.
    var videoElements = await page1.$$('video');
    expect(videoElements.length).toEqual(2);

    // Captura una captura de pantalla de la página 1 y la guarda en un archivo.
    await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });
    
    // Navega a una URL específica en la segunda página y realiza acciones similares a las de la primera página.
    await page2.goto('http://localhost:8080');
    await page2.fill('input[type="text"]', 'User2');
    await page2.click('button.btn.btn-lg.btn-success');
    await page2.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000);
  
    // Busca los elementos de video en la segunda página y verifica que haya exactamente dos de ellos.
    videoElements = await page2.$$('video');
    expect(videoElements.length).toEqual(3);

    // Captura una captura de pantalla de la página 2 y la guarda en un archivo.
    await page2.screenshot({ path: '../results/screenshots/page2_screenshot.png' });

    // Cierra las páginas y el navegador.
    await Promise.all([page1.close(), page2.close()]);
    await browser.close();

  } catch (error) {
    // En caso de error, captura las pantallas y registra el error.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    throw error; // Lanza el error nuevamente para que la prueba falle.
  }
});
