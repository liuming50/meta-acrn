require linux-acrn-common.inc

PV = "4.19.0-acrn+git${SRCPV}"

SRC_URI += "file://configs/local-version.cfg \
            file://configs/usb-kbd-mouse.cfg \
"
