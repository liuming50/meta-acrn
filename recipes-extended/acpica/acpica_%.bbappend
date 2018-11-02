# Hard-coded path /usr/sbin/iasl is being referred in ACRN source
do_install_append () {
    install -d ${D}${sbindir}
    mv ${D}${bindir}/iasl ${D}${sbindir}/iasl
}
