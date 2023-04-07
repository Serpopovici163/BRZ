import serial
import struct

#hex for start emulating
#\x50\x4D\x33\x61\x0F\x80\x0C\x02x00\x00\x00\x00\x20\x00\x00\x00\x86\x0C\xEC\x06\x00\x7F\x00\x61\x33

#start string 1
#\x50\x4D\x33\x61\x20\x80\x09\x01\x00\x01\x02\x03\x04\x05\x06\x07\x08\x09\x0A\x0B\x0C\x0D\x0E\x0F\x10\x11\x12\x13\x14\x15\x16\x17\x18\x19\x1A\x1B\x1C\x1D\x1E\x1F\x61\x33
#start string 2
#\x50\x4D\x33\x61\x00\x80\x12\x01\x61\x33
#start string 3
#\x50\x4D\x33\x61\x00\x80\x07\x01\x61\x33

ser = serial.Serial(
    port='COM4',
    baudrate=115200,
    bytesize=serial.EIGHTBITS,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE
)

print(ser.isOpen())
thestring = "50 4D 33 61 0F 80 0C 02 00 00 00 00 20 00 00 00 86 0C EC 06 00 7F 00 61 33"
#data = struct.pack(hex(thestring))
#data = struct.pack(hex, 0x7E, 0xFF, 0x03, 0x00, 0x01, 0x00, 0x02, 0x0A, 0x01, 0xC8,      0x04, 0xD0, 0x01, 0x02, 0x80, 0x00, 0x00, 0x00, 0x00, 0x8E, 0xE7, 0x7E)

#ser.write(str.encode("\x50\x4D\x33\x61\x20\x80\x09\x01\x00\x01\x02\x03\x04\x05\x06\x07\x08\x09\x0A\x0B\x0C\x0D\x0E\x0F\x10\x11\x12\x13\x14\x15\x16\x17\x18\x19\x1A\x1B\x1C\x1D\x1E\x1F\x61\x33"))
#ser.write(str.encode("\x50\x4D\x33\x61\x00\x80\x12\x01\x61\x33"))
#ser.write(str.encode("\x50\x4D\x33\x61\x00\x80\x07\x01\x61\x33"))
#ser.write(str.encode(thestring))

for hexByte in thestring.split(' '):
    print(hexByte)
    ser.write(int(hexByte,16).to_bytes(1, 'little'))

print("sent")

while True:
    s = ser.read()
    if len(s) == 0:
        break
    print (s)

print(s)
ser.close()