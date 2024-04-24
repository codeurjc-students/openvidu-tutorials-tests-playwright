const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({ 
    headless: true, 
    deviceScaleFactor: 1,
    userAgent: 'Chrome/88.0.4324.182', 
    args: ["--use-fake-ui-for-media-stream", "--use-fake-device-for-media-stream", "--ignore-certificate-errors"]
  });

  // Create a new context in the browser with necessary permissions for the first page.
  const context = await browser.newContext({
    permissions: ['camera', 'microphone'],
  });

  // Create a new page within the first context.
  const page1 = await context.newPage();
  
  // Create a new incognito context for the second page.
  const context2 = await browser.newContext({ incognito: true });
  
  // Create a new page within the second context.
  const page2 = await context2.newPage();

  try {
    // Navigate to a specific URL on the first page.
    await page1.goto('https://localhost:5000');

    // Perform actions on the first page.
    await page1.click('#join-btn');
    await page1.waitForSelector('#session', { visible: true });
    
    // Find video elements on the first page and verify there is exactly one.
    var videoElements = await page1.$$('video');
    
    expect(videoElements.length).toEqual(1);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);
     
    }
    

    // Capture a screenshot of the first page and save it to a file.
    await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });

    // Navigate to a specific URL on the second page.
    await page2.goto('https://localhost:5000');
                      
    // Perform actions on the second page.
    await page2.click('#join-btn');
    await page2.waitForSelector('#session', { visible: true });
    await page2.click('#buttonStartRecording');
    await page2.click('#buttonGetRecording');
    await page2.screenshot({ path: '../results/screenshots/page2_startrecording_screenshot.png' });
    await page2.waitForTimeout(5000);
    await page2.click('#buttonStopRecording');
    await page2.click('#buttonGetRecording');
    await page2.screenshot({ path: '../results/screenshots/page2_stoprecording_screenshot.png' });

    // Find video elements on the second page and verify there are exactly two.
        videoElements = await page2.$$('video');
    
    expect(videoElements.length).toEqual(2);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);

    }
    expect(videoElements.length).toEqual(2);
    
    // Close the pages and the browser.
    await Promise.all([page1.close(), page2.close()]);
  
  } catch (error) {
    // In case of error, capture screenshots and log the error.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    throw error; // Rethrow the error to make the test fail
  } 
});
