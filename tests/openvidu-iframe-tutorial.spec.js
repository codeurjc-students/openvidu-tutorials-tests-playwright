const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async () => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1, // Specify the page's scale factor
    userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36', // Specify the user agent
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
    await page1.goto('http://localhost:8081/');

    // Wait for the iframe with the class 'openvidu-iframe' and get its content frame.
    let iframeHandle = await page1.waitForSelector('iframe.openvidu-iframe');
    let frame = await iframeHandle.contentFrame();

    await page1.screenshot({ path: '../results/screenshots/page1_screensho.png' });

    // Click the "JOIN" button within the iframe and wait for the '#session' element to become visible.
    await frame.click('input[type="submit"]');
    await frame.waitForSelector('#session', { visible: true });

    // Navigate to the specified URL on page2.
    await page2.goto('http://localhost:8081/');
    iframeHandle = await page2.waitForSelector('iframe.openvidu-iframe');
    frame = await iframeHandle.contentFrame();

    // Click the "JOIN" button within the iframe, wait for the '#session' element to become visible, and wait for 5 seconds.
    await frame.click('input[type="submit"]');
    await frame.waitForSelector('#session', { visible: true });
    await frame.waitForTimeout(5000);

    // Capture a screenshot of page2 and save it to 'pr.png'.
    await page2.screenshot({ path: '../results/screenshots/page2_screensho.png' });

    // Find HTML elements within the frame that contain video streams.
    const videoElements = await frame.$$('video');

    // Check that there are exactly two elements found.
    expect(videoElements.length).toEqual(2);

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
