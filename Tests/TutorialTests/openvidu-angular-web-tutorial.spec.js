// Test for Angular tutorials.
const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1,
    userAgent: 'Chrome/88.0.4324.182',
    args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
  });

  // Create a new context with camera and microphone permissions.
  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });

  // Create two new pages within the context.
  const page1 = await context.newPage();
  const page2 = await context.newPage();

  try {
    
    // Navigate to the specified URL on page1.
    await page1.goto('http://localhost:4200/');

    // Fill in the username field, click the join button, and wait for the session to become visible.
    await page1.fill('#userName', 'User1');
    await page1.click('#join input[type="submit"]');
    await page1.waitForSelector('#session', { visible: true });
    await page1.waitForTimeout(5000);


    var videoElements = await page1.$$('video');
    
    expect(videoElements.length).toEqual(2);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);
     
    }

    // Capture a screenshot of page1 and save it to a file.
    await page1.screenshot({ path: '../../results/screenshots/page1_screenshot.png' });

    // Navigate to the specified URL on page2.
    await page2.goto('http://localhost:4200/');

    // Fill in the username field, click the join button, and wait for the session to become visible.
    await page2.fill('#userName', 'User2');
    await page2.click('#join input[type="submit"]');
    await page2.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000);

    
    // Find HTML elements that contain video streams on page2.
    videoElements = await page2.$$('video');
    
    // Check that there are exactly three elements found (should it be three or two?).
    expect(videoElements.length).toEqual(3);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);

    }

    await page2.screenshot({ path: '../../results/screenshots/page2_screenshot.png' });

    // Close the pages and the browser.
    await Promise.all([page1.close(), page2.close()]);
    await browser.close();
  } catch (error) {
    // In case of an error, capture screenshots and log the error.
    await page1.screenshot({ path: '../../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../../results/screenshots/error_page2_screenshot.png' });
    throw error; // Rethrow the error to make the test fail.
  }
});
