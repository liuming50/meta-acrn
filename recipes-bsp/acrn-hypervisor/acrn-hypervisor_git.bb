require acrn-common.inc

inherit python3native cml1 deploy

DEPENDS = "python3-pip-native python3-kconfiglib-native gnu-efi"

S = "${WORKDIR}/git/hypervisor"
B = "${S}/../build"

EXTRA_OEMAKE += "SYSROOT=${STAGING_DIR_TARGET}"

KCONFIG_CONFIG_COMMAND = "-C ${S} menuconfig"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} += "${libdir}/acrn/* ${datadir}/acrn/*"

USED_ACRN_CONFIG = "usedacrnconfig"

ACRN_UEFI_ARCH ?= "${@['ia32', 'x64'][d.getVar('TARGET_ARCH') == 'x86_64']}"
ACRN_UEFI_IMAGE_NAME ?= "boot${ACRN_UEFI_ARCH}"

do_configure () {
    cd ${S}/arch/x86/configs/
    cp ${MACHINE}.config ${USED_ACRN_CONFIG}.config
}

do_compile () {
    cd ${S}/..
    oe_runmake hypervisor BOARD=${USED_ACRN_CONFIG} FIRMWARE=${ACRN_PLATFORM}
}

do_install () {
    if [ "${ACRN_PLATFORM}" = "uefi" ]; then
        install -D -m 0755 ${B}/hypervisor/acrn.efi ${D}/${libdir}/acrn/acrn.efi
    fi
}

do_deploy () {
    if [ "${ACRN_PLATFORM}" = "uefi" ]; then
        install -m 0755 ${D}${libdir}/acrn/acrn.efi ${DEPLOYDIR}/acrn-${ACRN_UEFI_IMAGE_NAME}.efi
    fi
}

addtask deploy after do_install before do_build
