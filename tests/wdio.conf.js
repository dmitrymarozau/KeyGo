exports.config = {
  runner: 'local',
  port: 4723, // default Appium port
  specs: [
    './specs/**/*.spec.js'
  ],
  maxInstances: 1,
  services: ['appium'],
  capabilities: [{
    platformName: 'Android',
    'appium:automationName': 'UiAutomator2',
    'appium:deviceName': 'Android',
    'appium:app': '../app/build/outputs/apk/github/debug/app-github-debug.apk',
  }],
  framework: 'mocha',
  mochaOpts: {
    timeout: 60000,
  },
}
