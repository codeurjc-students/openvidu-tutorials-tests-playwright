//Test for Angular, React & Vue tutorials.
const { test, expect, chromium } = require('@playwright/test');



test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
const browser = await chromium.launch({ headless: true , deviceScaleFactor: 1, 
   userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', 
   args : ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
 });

   const context = await browser.newContext({
     permissions: ['camera', 'microphone'],
   });

   const page1 = await context.newPage();
   const page2 = await context.newPage();

   try {

   await page1.goto('http://localhost:4200');

   await page1.fill('#userName', 'User1');
   await page1.click('#join input[type="submit"]');
   await page1.waitForSelector('#session', { visible: true });

   await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });

   await page2.goto('http://localhost:4200');

   await page2.fill('#userName', 'User2');
   await page2.click('#join input[type="submit"]');
   await page2.waitForSelector('#session', { visible: true });
   await page2.waitForTimeout(5000); 
   
   await page2.screenshot({ path: '../results/screenshots/page2_screenshot.png' });

   
   // Buscar los elementos HTML que contienen los streams de video
   const videoElements = await page2.$$('video');

   // Comprobar que hay exactamente dos elementos encontrados
   expect(videoElements.length).toEqual(3);

   // Cerrar las páginas y el navegador.

  await Promise.all([page1.close(), page2.close()]);
  await browser.close();

  } catch (error) {
    // In case of an error, capture screenshots and log the error.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    throw error; // Rethrow the error to make the test fail.
  }
});
