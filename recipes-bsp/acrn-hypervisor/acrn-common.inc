SUMMARY = " A Type 1 reference hypervisor stack, running directly on the bare-metal hardware"
DESCRIPTION = "The open source project ACRN defines a device hypervisor reference stack \
and an architecture for running multiple software subsystems, managed \
securely, on a consolidated system by means of a virtual machine \
manager. It also defines a reference framework implementation for \
virtual device emulation, called the "ACRN Device Model"."
HOMEPAGE = "https://projectacrn.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/../LICENSE;md5=5732af37bf18525ed9d2b16985054901"

SRC_URI = "git://github.com/projectacrn/acrn-hypervisor.git"

SRCREV = "8b64e336afd5c0f019175ea482e934a3d995ed8e"
PV = "0.4+git${SRCPV}"

UPSTREAM_CHECK_COMMITS = "1"

ACRN_RELEASE = "0"

EXTRA_OEMAKE += "RELEASE=${ACRN_RELEASE}"
