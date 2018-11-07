require acrn-common.inc

inherit python3native cml1 deploy

DEPENDS = "python3-pip-native python3-kconfiglib-native gnu-efi"

S = "${WORKDIR}/git/hypervisor"
B = "${S}/build"

EXTRA_OEMAKE += "SYSROOT=${STAGING_DIR_TARGET}"

KCONFIG_CONFIG_COMMAND = "-C ${S} menuconfig"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} += "${libdir}/acrn/* ${datadir}/acrn/*"

do_configure_prepend () {
    cd ${S}
    oe_runmake defconfig PLATFORM=${ACRN_PLATFORM}

    if [ "${ACRN_PLATFORM}" = "uefi" ]; then
        sed -i -e 's#^\(CONFIG_UEFI_OS_LOADER_NAME=\).*$#\1\"\\\\EFI\\\\${ACRN_UEFI_OS_LOADER_DIR}\\\\${ACRN_UEFI_OS_LOADER_NAME}.efi\"#' ${B}/.config
    fi
}

do_compile () {
    cd ${S}
    oe_runmake
}

do_install () {
    cd ${S}
    oe_runmake install DESTDIR=${D}
}

do_deploy () {
    if [ "${ACRN_PLATFORM}" = "uefi" ]; then
        install -m 0755 ${D}${libdir}/acrn/acrn.efi ${DEPLOYDIR}/acrn-${ACRN_UEFI_IMAGE_NAME}-${MACHINE}-${DATETIME}.efi
        ln -sf acrn-${ACRN_UEFI_IMAGE_NAME}-${MACHINE}-${DATETIME}.efi ${DEPLOYDIR}/acrn-${ACRN_UEFI_IMAGE_NAME}-${MACHINE}.efi
        ln -sf acrn-${ACRN_UEFI_IMAGE_NAME}-${MACHINE}-${DATETIME}.efi ${DEPLOYDIR}/acrn-${ACRN_UEFI_IMAGE_NAME}.efi
    fi
}
do_deploy[vardepsexclude] = "DATETIME"

addtask deploy after do_install before do_build
