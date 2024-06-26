const {Builder, By, Key, until, WebDriverWait, WebElement} = require('selenium-webdriver');

let driver;

beforeEach(async () => {
    driver = await new Builder().forBrowser('chrome').build();
})


test('User Register', async () => {
    let driver = await new Builder().forBrowser('chrome').build();
    await driver.manage().setTimeouts({
        implicit: 10000 // 10 seconds
    });
    const randomId = Math.floor(Math.random() * 1000) + 1;
    await driver.get('http://127.0.0.1:5173/');
    await driver.manage().window().maximize();
    await driver.findElement(By.className('sign-up-nav')).click()
    await driver.findElement(By.id('name')).sendKeys('Sara')
    await driver.findElement(By.id('last-name')).sendKeys('Lopez')
    await driver.findElement(By.id('email')).sendKeys(`saralohe${randomId}@test-test.com`)
    await driver.findElement(By.id('pass')).sendKeys('123Sara+')
    await driver.findElement(By.id('sign-up-button')).click();
    while (true)
    {
        try {
            await driver.switchTo().alert().accept();
            break;
        } catch (e) {
            await driver.sleep(100);
        }
    }
    const currentURL = await driver.getCurrentUrl();
    expect(currentURL).toContain('loginUser');
    await driver.quit();

});

