const { test, expect, chromium } = require('@playwright/test');

test('Checking for the presence of two active webcams in an OpenVidu session', async ({ page }) => {
  // Launch a headless Chromium browser with specific settings.
  const browser = await chromium.launch({
    headless: true,
    deviceScaleFactor: 1,
    userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36',
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
    await page1.goto('http://127.0.0.1:8080');

    // Wait for the 'button.btn-success' to be present on the page, with a timeout of 10 seconds.
    await page1.waitForSelector('button.btn-success', { timeout: 10000 });

    // Click the button with the 'onclick' attribute.
    await page1.click('button.btn-success');
    
    // Wait for the '#session' element to become visible and wait for 5 seconds.
    await page1.waitForSelector('#session', { visible: true });
    await page1.waitForTimeout(5000);

    // Get the value of the 'input#copy-input' element.
    const inputValue = await page1.$eval('input#copy-input', (input) => input.value);

    // Capture a screenshot of page1 and save it to a file.
    await page1.screenshot({ path: '../results/screenshots/page1_screenshot.png' });
    
    // Navigate to the URL extracted from 'inputValue' on page2.
    await page2.goto(inputValue);

    // Wait for the '#session' element to become visible and wait for 5 seconds.
    await page2.waitForSelector('#session', { visible: true });
    await page2.waitForTimeout(5000);
    
    // Capture a screenshot of page2 and save it to a file.

    await page2.screenshot({ path: '../results/screenshots/page2_screenshot.png' });

    // Find HTML elements that contain video streams on page2.
    const videoElements = await page2.$$('video');
    
    // Check that there are exactly two elements found.
    expect(videoElements.length).toEqual(2);
    
    // Close page1 and the browser.
    await Promise.all([page1.close(), browser.close()]);

  } catch (error) {
    // In case of an error or timeout, capture a screenshot.
    await page1.screenshot({ path: '../results/screenshots/error_page1_screenshot.png' });
    await page2.screenshot({ path: '../results/screenshots/error_page2_screenshot.png' });
    
    // Rethrow the error to make the test fail.
    throw error;
  } 
});
