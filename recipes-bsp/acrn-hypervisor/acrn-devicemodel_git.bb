require acrn-common.inc

inherit python3native systemd distro_features_check

REQUIRED_DISTRO_FEATURES = "systemd"

DEPENDS = "systemd openssl libpciaccess acrn-tools python3-kconfiglib-native"

S = "${WORKDIR}/git/devicemodel"

EXTRA_OEMAKE += "SYSROOT=${STAGING_DIR_TARGET} TOOLS_OUT=${STAGING_DIR_TARGET}${includedir}/acrn"

TARGET_LDFLAGS += "-L${STAGING_DIR_TARGET}/{libdir}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} += "bash acpica procps"

FILES_${PN} += "${systemd_unitdir}/* ${datadir}/acrn/*"

do_install () {
    oe_runmake install DESTDIR=${D}

    install -d ${D}${systemd_unitdir}
    mv ${D}${libdir}/systemd/* ${D}${systemd_unitdir}/
    rm -rf ${D}${libdir}
}
