it('Check that Appium and WebDriverIO are working', async () => {
  expect(await $('android=new UiSelector().text("KeyGo")').isDisplayed()).toBe(true);
});
