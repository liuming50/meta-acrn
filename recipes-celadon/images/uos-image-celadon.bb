DESCRIPTION = "Celadon Android user image"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_DEFAULT_DEPS = "1"
EXCLUDE_FROM_WORLD = "1"

inherit externalsrc deploy nopackages

EXTERNALSRC ?= "/opt/acrn-yocto/celadon"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"
EXTERNALSRC_SYMLINKS = ""

CELADON_IMAGE_BASENAME ?= "${PN}"
CELADON_IMAGE_NAME ?= "${CELADON_IMAGE_BASENAME}-${MACHINE}-${DATETIME}"
CELADON_IMAGE_NAME[vardepsexclude] = "DATETIME"
CELADON_IMAGE_LINK_NAME ?= "${CELADON_IMAGE_BASENAME}-${MACHINE}"
CELADON_IMAGE_BUILD_PATH ?= "${EXTERNALSRC_BUILD}/out/target/product/celadon"

CELADON_SOURCES[type] = "list"
CELADON_SOURCES = " \
    Makefile \
    art \
    bionic \
    bootable \
    build \
    compatibility \
    cts \
    dalvik \
    developers \
    development \
    device \
    external \
    frameworks \
    hardware \
    kernel \
    libcore \
    libnativehelper \
    packages \
    pdk \
    platform_testing \
    prebuilts \
    sdk \
    system \
    test \
    toolchain \
    tools \
    trusty \
    vendor \
"

def srctree_hash_files(d, srcdir=None):
    import shutil
    import subprocess
    import tempfile

    s_dir = srcdir or d.getVar('EXTERNALSRC')
    s_files = (d.getVar('CELADON_SOURCES') or "").split()
    subprocess.check_output(['git', 'init'], cwd=s_dir)
    subprocess.check_output(['git', 'config', 'user.email', 'builder@docker-builder.com'], cwd=s_dir)
    subprocess.check_output(['git', 'config', 'user.name', 'builder'], cwd=s_dir)
    subprocess.check_output(['git', 'add'] + s_files, cwd=s_dir)
    sha1 = subprocess.check_output(['git', 'write-tree'], cwd=s_dir).decode("utf-8")

    oe_hash_file = os.path.join(s_dir, 'oe-devtool-tree-sha1')
    with open(oe_hash_file, 'w') as fobj:
        fobj.write(sha1)
    ret = oe_hash_file + ':True'
    shutil.rmtree(os.path.join(s_dir, '.git'))
    return ret

do_buildclean() {
    if [ -e Makefile -o -e makefile -o -e GNUmakefile ]; then
        rm -f ${@' '.join([x.split(':')[0] for x in (d.getVar('EXTERNALSRC_SYMLINKS') or '').split()])}
        if [ "${CLEANBROKEN}" != "1" ]; then
            oe_runmake clobber || die "make failed"
        fi
    else
        bbnote "nothing to do - no makefile found"
    fi
}

do_normalize_hostcc () {
    cd ${HOSTTOOLS_DIR}
    ln -sf gcc cc
}

addtask normalize_hostcc before do_configure

do_compile () {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    . build/envsetup.sh
    lunch celadon-userdebug
    oe_runmake SPARSE_IMG=true project_celadon-efi
}

do_deploy () {
    install -m 0644 ${CELADON_IMAGE_BUILD_PATH}/celadon.img ${DEPLOYDIR}/${CELADON_IMAGE_NAME}.img
    ln -sf ${CELADON_IMAGE_NAME}.img ${DEPLOYDIR}/${CELADON_IMAGE_LINK_NAME}.img
    ln -sf ${CELADON_IMAGE_NAME}.img ${DEPLOYDIR}/${CELADON_IMAGE_BASENAME}.img
}
do_deploy[vardepsexclude] = "DATETIME"

addtask deploy after do_install before do_build
