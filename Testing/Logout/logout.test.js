const {Builder, By, Key, until} = require('selenium-webdriver');

let driver;

beforeEach(async () => {
    driver = await new Builder().forBrowser('chrome').build();
})


test('Logout', async () => {
    let driver = await new Builder().forBrowser('chrome').build();
    await driver.manage().setTimeouts({
        implicit: 10000 // 10 seconds
    });

    await driver.get('http://127.0.0.1:5173/');
    await driver.manage().window().maximize();
    await driver.findElement(By.className('sign-in-nav')).click()
    await driver.findElement(By.id('email')).sendKeys('saralohe8@gmail.com')
    await driver.findElement(By.id('pass')).sendKeys('SaraLopez1+')
    await driver.findElement(By.id('login-button')).click()
    await driver.findElement(By.className('log-out-nav')).click();
    const alert = await driver.switchTo().alert();
    await alert.accept();
    const signInElement = await driver.findElement(By.className('sign-in-nav'));
    expect(signInElement).toBeTruthy()
    await driver.quit();

});

