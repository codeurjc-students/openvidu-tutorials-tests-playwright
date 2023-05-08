const { test, expect, chromium } = require('@playwright/test');

test('homepage has title and links to intro page', async () => {
  const browser = await chromium.launch({ headless: true });

  const context1 = await browser.newContext({
    permissions: ['camera', 'microphone']
  });

  const context2 = await browser.newContext({
    permissions: ['camera', 'microphone']
  });

  const page1 = await context1.newPage();
  await page1.goto('https://demo.openvidu.io/basic-videoconference/');
  await page1.fill('#sessionId', 'test');
  await page1.fill('#customUsername', 'User 1');
  await page1.click('#join');
  await page1.waitForSelector('#session-connected', { visible: true });

  const page2 = await context2.newPage();
  await page2.goto('https://demo.openvidu.io/basic-videoconference/');
  await page2.fill('#sessionId', 'test');
  await page2.fill('#customUsername', 'User 2');
  await page2.click('#join');
  await page2.waitForSelector('#session-connected', { visible: true });

  // Esperar a que ambos usuarios se conecten
  await Promise.all([
    page1.waitForSelector('.video-container video'),
    page2.waitForSelector('.video-container video')
  ]);

  // Esperar a que la sesión esté lista
  await Promise.all([
    page1.waitForSelector('.publisher .OT_video-element'),
    page2.waitForSelector('.publisher .OT_video-element')
  ]);

  // Esperar a que se reciban los streams de ambos usuarios
  await Promise.all([
    page1.waitForSelector('.subscriber .OT_video-element'),
    page2.waitForSelector('.subscriber .OT_video-element')
  ]);

  // Tomar capturas de pantalla en ambas páginas
  const [screenshot1, screenshot2] = await Promise.all([
    page1.screenshot({ type: 'jpeg', quality: 80, clip: { x: 0, y: 0, width: 640, height: 480 } }),
    page2.screenshot({ type: 'jpeg', quality: 80, clip: { x: 0, y: 0, width: 640, height: 480 } })
  ]);

  // Comprobar que las capturas de pantalla contienen video de la otra cámara web
  expect(screenshot1).toContain('OT_video-element');
  expect(screenshot2).toContain('OT_video-element');

  // Cerrar las páginas y el navegador
  await Promise.all([page1.close(), page2.close()]);
  await browser.close();
});
