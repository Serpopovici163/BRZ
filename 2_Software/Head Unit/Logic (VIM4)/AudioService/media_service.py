#command for media controls
#dbus-send --system --print-reply --dest=org.bluez /org/bluez/hci0/dev_DC_E5_5B_1B_90_FE org.bluez.MediaControl1.Play
#from: https://askubuntu.com/questions/1113050/sending-pause-resume-playing-and-next-previous-track-bluetooth-commands

'''
Possible command list:

FastForward
Rewind
Play
Pause
Stop
Next
Previous
VolumeUp
VolumeDown

TODO: add "GET" commands and try to get media name/artist name if possible

'''

# Python 3 server example pulled from https://pythonbasics.org/webserver/
from http.server import BaseHTTPRequestHandler, HTTPServer
import time
import os
import dbus

#adjust as necessary, localhost does not seem to allow for remote connections
hostName = "localhost"
serverPort = 12345
deviceMAC = "DC_E5_5B_1B_90_FE"    

#for media info
bus = dbus.SystemBus()
adapter_object = bus.get_object('org.bluez', '/org/bluez/hci0')
adapter = dbus.Interface(adapter_object, 'org.bluez.Adapter1')

device_object = bus.get_object("org.bluez", "/org/bluez/hci0/dev_DC_E5_5B_1B_90_FE/player0")
device = dbus.Interface(device_object, "org.bluez.MediaPlayer1")

device_properties = dbus.Interface(device, "org.freedesktop.DBus.Properties")

def getAllAttributes():
    attribDict = device_properties.GetAll("org.bluez.MediaPlayer1")
    # get status, position, [track]{title, duration, artist}
    # return REQUEST_OK;status;position;duration;artist;title
    return "REQUEST_OK;" + attribDict["Status"] + ";" + str(attribDict["Position"]) + ";" + str(attribDict["Track"]["Duration"]) + ";" + attribDict["Track"]["Artist"] + ";" + attribDict["Track"]["Title"]

def getPosition():
    return "REQUEST_OK;" + str(device_properties.GetAll("org.bluez.MediaPlayer1")["Position"])

def removeHex(data):
    data = data.decode("utf-8")
    while data.find("%") != -1:
        index = data.find("%")
        string = data[index + 1]
        string += data[index + 2]
        searchString = data[index]
        searchString += data[index + 1]
        searchString += data[index + 2]
    
        if (searchString == "%0A"): #0A represents new line but we do not want a new line char in our data
            data = data.replace(searchString, "")
        else:
            data = data.replace(searchString, bytes.fromhex(string).decode("utf-8"))
    return data

class MyServer(BaseHTTPRequestHandler):
    def do_POST(self):

        #first check if device is connected and escape if not
        if (dbus.Interface(dbus.Interface(bus.get_object("org.bluez", "/org/bluez/hci0/dev_DC_E5_5B_1B_90_FE"), "org.bluez.Device1"), "org.freedesktop.DBus.Properties").GetAll("org.bluez.Device1")["Connected"] != 1):
            return

        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        #remove hex characters and replace with ASCII counterparts
        post_data = removeHex(post_data)
        post_data = post_data.replace("data=","")

        if (post_data == "GET_ALL_ATTRIBUTES"):
            getAllAttributes()
        elif (post_data == "GET_POSITION"):
            getPosition()
        else:
            os.system("dbus-send --system --print-reply --dest=org.bluez /org/bluez/hci0/dev_" + deviceMAC + " org.bluez.MediaControl1." + post_data)
      
        self.send_response(200)
        self.end_headers()

if __name__ == "__main__":        
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")