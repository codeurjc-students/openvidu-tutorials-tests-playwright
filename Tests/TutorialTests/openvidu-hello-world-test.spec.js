const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1,
    userAgent: 'Chrome/88.0.4324.182', // Specify the user agent
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
    // Navigate to a specific URL on page 1.
    await page1.goto('http://localhost:8080');
    

    // Interact with page1, such as clicking a button and waiting for a specific element to become visible, then wait for a timeout..
    await page1.click('#join input[type="submit"]');
    await page1.waitForSelector('#session', { visible: true });
    await page1.waitForTimeout(5000);

    var videoElements = await page1.$$('video');
    
    expect(videoElements.length).toEqual(1);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);
     
    }
     
    // Capture a screenshot of page1 and save it to a file.
    await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });

    // Navigate to a specific URL on page 1.
    await page2.goto('http://localhost:8080');

    // Interact with page2 and wait for a specific element to become visible, then wait for a timeout.
    await page2.click('#join input[type="submit"]');
    await page2.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000);

    // Capture a screenshot of page2 and save it to a file.
    await page2.screenshot({ path: '../results/screenshots/page2_screenshot.png' });

    // Find HTML elements containing video streams on page2 and check if there are exactly two of them.
    videoElements = await page2.$$('video');
    
    expect(videoElements.length).toEqual(2);

    for (const videoElement of videoElements) {
      
      const isPaused = await videoElement.evaluate(video => video.paused);
      
      expect(isPaused).not.toBe(true);

    }
 
    var audioElement = await page2.$('audio');
    expect(audioElement).not.toBe(null);

    var isMuted = await audioElement.evaluate(audio => audio.muted);
    expect(isMuted).not.toBe(true);


    // Close the pages and the browser.
    await Promise.all([page1.close(), page2.close()]);
    await browser.close();

  } catch (error) {
    // In case of an error, capture screenshots and log the error.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    throw error; // Rethrow the error to make the test fail.
  }
});
