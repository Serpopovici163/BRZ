const { app, BrowserWindow , screen} = require('electron')

const createWindow = () => {
    const media_win = new BrowserWindow({
      width: 2880,
      height: 864
    })

    const diagnostic_win = new BrowserWindow({
        width: 1200,
        height: 1920
      })
  
    media_win.loadFile('media_display.html')
    diagnostic_win.loadFile('center_console_display.html')
}

app.whenReady().then(() => {
    createWindow()  
})