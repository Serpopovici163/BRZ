#!/usr/bin/env python
#https://github.com/ancabilloni/udp_camera_streaming

from __future__ import division
import cv2
import numpy as np
import socket
import struct
import math
import _thread


class FrameSegment(object):
    """ 
    Object to break down image frame segment
    if the size of image exceed maximum datagram size 
    """
    MAX_DGRAM = 2**16
    MAX_IMAGE_DGRAM = MAX_DGRAM - 64 # extract 64 bytes in case UDP frame overflown
    def __init__(self, sock, port, addr="192.168.23.3"):
        self.s = sock
        self.port = port
        self.addr = addr

    def udp_frame(self, img):
        """ 
        Compress image and Break down
        into data segments 
        """
        compress_img = cv2.imencode('.jpg', img)[1]
        dat = compress_img.tobytes()
        size = len(dat)
        count = math.ceil(size/(self.MAX_IMAGE_DGRAM))
        array_pos_start = 0
        while count:  
            array_pos_end = min(size, array_pos_start + self.MAX_IMAGE_DGRAM)
            self.s.sendto(struct.pack("B", count) +
                dat[array_pos_start:array_pos_end], 
                (self.addr, self.port)
                )
            array_pos_start = array_pos_end
            count -= 1


def broadcast_feed(port, captureID):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print("Socket [" + str(captureID) + "] OK")
    fs = FrameSegment(s, port)
    print("FrameSegment [" + str(captureID) + "] OK")
    cap = cv2.VideoCapture(captureID)
    print("VideoCapture [" + str(captureID) + "] OK")
    while (cap.isOpened()):
        _, frame = cap.read()
        fs.udp_frame(frame)
    cap.release()
    s.close

def main():
    """ Top level main function """
    try:
        print("Launching threads")
        _thread.start_new_thread(broadcast_feed, (12345, 4, ))
        _thread.start_new_thread(broadcast_feed, (12346, 1, ))
        _thread.start_new_thread(broadcast_feed, (12347, 2, ))
        _thread.start_new_thread(broadcast_feed, (12348, 3, ))
        print("Threads launched")
        while 1:
            pass
    except:
        print("Thread error")
    
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()