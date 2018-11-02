require acrn-common.inc

inherit pkgconfig systemd distro_features_check

REQUIRED_DISTRO_FEATURES = "systemd"

DEPENDS = "systemd libevent openssl libxml2 libusb1 util-linux e2fsprogs"

S = "${WORKDIR}/git/tools"

EXTRA_OEMAKE += "SYSROOT=${STAGING_DIR_TARGET}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SYSTEMD_SERVICE_${PN} = "acrnd.service acrnlog.service"
# FIXME: should enable crashlog tools for debug build
# SYSTEMD_SERVICE_${PN} += "${@['', 'usercrash.service acrnprobe.service'][d.getVar('ACRN_RELEASE') == '0']}"

RDEPENDS_${PN} += "bash"

FILES_${PN} += "${systemd_unitdir}/* ${libdir}/tmpfiles.d ${libdir}/acrn ${datadir}/acrn ${datadir}/defaults /opt/acrn/conf"

INSANE_SKIP_${PN} += "ldflags"

do_install () {
    oe_runmake install DESTDIR=${D}

    install -d ${D}/opt/acrn/conf

    install -d ${D}${systemd_unitdir}
    mv ${D}${libdir}/systemd/* ${D}${systemd_unitdir}/
    rm -rf ${D}${libdir}/systemd

    mv ${D}${prefix}/lib64/* ${D}${libdir}
    rm -rf ${D}${prefix}/lib64
}
