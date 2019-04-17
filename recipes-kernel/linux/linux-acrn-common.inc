SUMMARY = "Linux Kernel provided and supported by ACRN project"
DESCRIPTION = "Linux Kernel provided and supported by ACRN project"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRC_URI = "git://github.com/projectacrn/acrn-kernel.git;protocol=git;branch=acrn_2109ww07"

SRCREV = "b4d85fc3870aa160124eb784f79325ad9ad38ab3"

inherit kernel

S = "${WORKDIR}/git"

DEPENDS += "lz4-native lzop-native bc-native elfutils-native"

SCMVERSION ??= "y"

do_preconfigure () {
    cat ${WORKDIR}/configs/*.cfg 2>/dev/null | while read line; do
        config=$(echo "$line" | grep -o 'CONFIG_[^= ]*')
        sed -i "/$config[ =]/d;" ${WORKDIR}/defconfig
        echo "$line" >> ${WORKDIR}/defconfig
    done
}
addtask preconfigure before do_configure after do_unpack do_patch

do_copy_defconfig () {
    if [ -e "${S}/${KERNEL_DEFCONFIG}" ]; then
        cp ${S}/${KERNEL_DEFCONFIG} ${WORKDIR}/defconfig
    fi
}
addtask copy_defconfig after do_unpack before do_preconfigure

do_add_scmversion () {
    if [ "${SCMVERSION}" = "y" ]; then
        # Add GIT revision to the local version
        head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
        printf "%s%s" +g $head > ${S}/.scmversion
    fi
}
addtask add_scmversion after do_unpack before do_configure
