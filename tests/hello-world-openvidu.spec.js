// @ts-check
const { test, expect } = require('@playwright/test');

test('homepage has title and links to intro page', async ({ page }) => {
  await page.goto('http://localhost:8080');

  // Expect a title "to contain" a substring.
  await expect(page).toHaveTitle(/openvidu-hello-world/);

  // Makin a screenshot of full page
  await page.screenshot({ path: 'screenshot.png', fullPage: true });

  // create a locator
  //const getStarted = page.getByRole('link', { name: 'Join' });

  // Expect an attribute "to be strictly equal" to the value.
  //await expect(getStarted).toHaveAttribute('href', '/docs/intro');

  // Click the get started link.
  //await getStarted.click();
  
  // Expects the URL to contain intro.
  //await expect(page).toHaveURL(/.*intro/);
});
