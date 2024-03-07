#!/usr/bin/env python3

import dbus

bus = dbus.SystemBus()
adapter_object = bus.get_object('org.bluez', '/org/bluez/hci0')
adapter = dbus.Interface(adapter_object, 'org.bluez.Adapter1')

device_object = bus.get_object("org.bluez", "/org/bluez/hci0/dev_DC_E5_5B_1B_90_FE/player0")
device = dbus.Interface(device_object, "org.bluez.MediaPlayer1")

device_properties = dbus.Interface(device, "org.freedesktop.DBus.Properties")
print(device_properties.GetAll("org.bluez.MediaPlayer1")["Track"]["Title"])
