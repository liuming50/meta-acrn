require linux-acrn-common.inc

PV = "4.19.0-acrn-uos+git${SRCPV}"

SRC_URI += "file://configs/local-version.cfg"

KERNEL_DEFCONFIG = "kernel_config_uos"
