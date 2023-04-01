#!/usr/bin/env python
#https://github.com/ancabilloni/udp_camera_streaming

from __future__ import division
import cv2
import numpy as np
import socket
import struct
import math


class FrameSegment(object):
    """ 
    Object to break down image frame segment
    if the size of image exceed maximum datagram size 
    """
    MAX_DGRAM = 2**16
    MAX_IMAGE_DGRAM = MAX_DGRAM - 64 # extract 64 bytes in case UDP frame overflown
    def __init__(self, sock, port, addr="127.0.0.1"):
        self.s = sock
        self.port = port
        self.addr = addr

    def udp_frame(self, img):
        """ 
        Compress image and Break down
        into data segments 
        """
        compress_img = cv2.imencode('.jpg', img)[1]
        dat = compress_img.tostring()
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


def main():
    """ Top level main function """
    # Set up UDP socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    port = 12345

    s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    port1 = 12346

    fs = FrameSegment(s, port)
    fs1 = FrameSegment(s1, port1)

    cap = cv2.VideoCapture(1)
    cap1 = cv2.VideoCapture(0)
    while (cap.isOpened() and cap1.isOpened()):
        _, frame = cap.read()
        _, frame1 = cap1.read()
        fs.udp_frame(frame)
        fs1.udp_frame(frame1)
    cap.release()
    cap1.release()
    cv2.destroyAllWindows()
    s.close()
    s1.close()

if __name__ == "__main__":
    main()