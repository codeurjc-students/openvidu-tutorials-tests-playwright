const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1,
    userAgent: 'Chrome/88.0.4324.182',
    args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream"]
  });

  // Create a new context in the browser with necessary permissions.
  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });

  // Create two new pages within the browser context.
  const page1 = await context.newPage();
  const page2 = await context.newPage();

  try {
    // Navigate to a specific URL on page 1 and perform actions such as filling a text field and clicking a button.
    await page1.goto('http://localhost:8080');
    await page1.fill('input[type="text"]', 'User1');

    await page1.click('button.btn.btn-lg.btn-success');
    
    // Wait for a specific element to become visible.
    await page1.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000);

    // Find video elements on page 1 and verify there are exactly two of them.
    var videoElements = await page1.$$('video');
    
    expect(videoElements.length).toEqual(1);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);
     
    }
    expect(videoElements.length).toEqual(2);

    // Capture a screenshot of page 1 and save it to a file.
    await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });
    
    // Navigate to a specific URL on page 2 and perform similar actions to page 1.
    await page2.goto('http://localhost:8080');
    await page2.fill('input[type="text"]', 'User2');
    await page2.click('button.btn.btn-lg.btn-success');
    await page2.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000);
  
    // Find video elements on page 2 and verify there are exactly two of them.
        videoElements = await page2.$$('video');
    
    expect(videoElements.length).toEqual(2);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);

    }
    expect(videoElements.length).toEqual(3);

    // Capture a screenshot of page 2 and save it to a file.
    await page2.screenshot({ path: '../results/screenshots/page2_screenshot.png' });

    // Close the pages and the browser.
    await Promise.all([page1.close(), page2.close()]);
    await browser.close();

  } catch (error) {
    // In case of error, capture screenshots and log the error.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    throw error; // Rethrow the error to make the test fail.
  }
});
