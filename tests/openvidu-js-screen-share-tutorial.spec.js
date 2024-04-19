const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1, // Specify the page's scale factor
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
    // Navigate to the specified URL on page1.
    await page1.goto('http://localhost:8080');

    // Fill in the '#userName' field with 'Page1', click the 'JOIN' button, and wait for the '#session' element to become visible.
    await page1.fill('#userName', 'Page1');
    await page1.click('#join input[type="submit"]');
    await page1.waitForSelector('#session', { visible: true });
    await page1.waitForTimeout(5000);

    // Capture a screenshot of page1 and save it to 'results/screenshot/page1.png'.
    await page1.screenshot({ path: '../results/screenshots/page1.png' });

    // Navigate to the specified URL on page2.
    await page2.goto('http://localhost:8080');

    // Fill in the '#userName' field with 'Page2', click the 'JOIN' button, wait for the '#buttonScreenShare' element to become visible, and click it.
    await page2.fill('#userName', 'Page2');
    await page2.click('#join input[type="submit"]');
    await page2.waitForSelector('#buttonScreenShare', { state: 'visible' });
    await page2.click('#buttonScreenShare');
    await page2.waitForTimeout(5000);

    // Capture a screenshot of page2 and save it to 'results/screenshot/page2.png'.
    await page2.screenshot({ path: '../results/screenshots/page2.png' });

    // Find HTML elements within page2 that contain video streams.
    const videoElements = await page2.$$('video');

    // Check that there are exactly four elements found.
    expect(videoElements.length).toEqual(4);

    // Close the pages and the browser.
    await Promise.all([page1.close(), page2.close()]);
    await browser.close();
  } catch (error) {
    // In case of an error or timeout, capture screenshots.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });

    // Rethrow the error to make the test fail.
    throw error;
  }
});
