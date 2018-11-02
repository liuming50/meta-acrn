# Skip processing of this recipe if linux-acrn is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-acrn":
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_virtual/kernel to linux-acrn to enable it")
}

IMAGE_FSTYPES = "wic"

CORE_IMAGE_EXTRA_INSTALL_append = " \
    uos-kernel \
    uos-kernel-image \
    uos-kernel-modules \
"

# Drop SOS kernel artifacts
PACKAGE_EXCLUDE += " \
    kernel \
    kernel-image \
    ${@' '.join(['kernel-image-' + t for t in d.getVar('KERNEL_IMAGETYPES').split()])} \
    kernel-modules \
"

# Even though the SOS kernel artifacts have been excluded by setting PACKAGE_EXCLUDE, they
# still could be installed through being depended by other packages. (Like kernel-module-*)
remove_sos_kernel_artifacts () {
    # Remove SOS kernel modules
    for dir in `find ${IMAGE_ROOTFS}/lib/modules -maxdepth 1 ! -name "*-uos*"`; do
        if [ $dir != "${IMAGE_ROOTFS}/lib/modules" ]; then
            rm -rf $dir
        fi
    done

    # Remove SOS kernel images
    for img in `find ${IMAGE_ROOTFS}/boot -maxdepth 1 ! -name "*-uos*"`; do
        if [ $img != "${IMAGE_ROOTFS}/boot" ]; then
            rm -f $img
        fi
    done

    for ktype in ${KERNEL_IMAGETYPES}; do
        img=`cd ${IMAGE_ROOTFS}/boot; ls $ktype-*uos*`
        if [ -f ${IMAGE_ROOTFS}/boot/$img ]; then
            ln -sf $img ${IMAGE_ROOTFS}/boot/$ktype
        fi
    done
}

IMAGE_PREPROCESS_COMMAND += "remove_sos_kernel_artifacts ; "
