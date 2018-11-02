require acrn-common.inc

SRC_URI += "file://acrn-guest@.service \
            file://launch_uos.sh \
           "

inherit systemd distro_features_check

REQUIRED_DISTRO_FEATURES = "systemd"

DEPENDS = "systemd openssl libpciaccess acrn-tools"

S = "${WORKDIR}/git/devicemodel"

EXTRA_OEMAKE += "SYSROOT=${STAGING_DIR_TARGET} TOOLS_OUT=${STAGING_DIR_TARGET}${includedir}/acrn"

TARGET_LDFLAGS += "-L${STAGING_DIR_TARGET}/{libdir}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} += "bash acpica procps"

SYSTEMD_SERVICE_${PN} = "acrn-guest@.service"

FILES_${PN} += "${systemd_unitdir}/* ${datadir}/acrn/*"

do_install () {
    oe_runmake install DESTDIR=${D}

    install -d ${D}${systemd_unitdir}
    mv ${D}${libdir}/systemd/* ${D}${systemd_unitdir}/
    rm -rf ${D}${libdir}

    install -d ${D}${datadir}/acrn/scripts
    install -m 0755 ${WORKDIR}/launch_uos.sh ${D}${datadir}/acrn/scripts

    rm -f ${D}${systemd_unitdir}/system/acrn_guest.service
    install -m 0755 ${WORKDIR}/acrn-guest@.service ${D}${systemd_unitdir}/system
}
