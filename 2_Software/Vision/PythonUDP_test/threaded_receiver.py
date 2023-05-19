#!/usr/bin/env python
#https://github.com/ancabilloni/udp_camera_streaming

from __future__ import division
import cv2
import numpy as np
import socket
import struct
import _thread

MAX_DGRAM = 2**16

def dump_buffer(s):
    """ Emptying buffer frame """
    while True:
        seg, addr = s.recvfrom(MAX_DGRAM)
        print(seg[0])
        if struct.unpack("B", seg[0:1])[0] == 1:
            print("finish emptying buffer")
            break

def show_feed(port):
    # Set up socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print("Socket [" + str(port) + "] OK")
    s.bind(('192.168.23.3', port))
    print("Socket [" + str(port) + "] BOUND")
    dat = b''
    dump_buffer(s)
    print("Buffer [" + str(port) + "] DUMPED")

    while True:
        seg, addr = s.recvfrom(MAX_DGRAM)
        if struct.unpack("B", seg[0:1])[0] > 1:
            dat += seg[1:]
        else:
            dat += seg[1:]
            img = cv2.imdecode(np.fromstring(dat, dtype=np.uint8), 1)
            try:
                cv2.imshow('frame' + str(port), img)
            except:
                pass
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
            dat = b''

    s.close()

def main():
    """ Getting image udp frame &
    concatenate before decode and output image """

    try:
        print("Launching threads")
        _thread.start_new_thread(show_feed, (12345, ))
        _thread.start_new_thread(show_feed, (12346, ))
        _thread.start_new_thread(show_feed, (12347, ))
        _thread.start_new_thread(show_feed, (12348, ))
        print("Threads launched")
        while 1:
            pass
    except:
        print("Thread error")

    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()
