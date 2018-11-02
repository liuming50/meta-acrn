# Skip processing of this recipe if linux-acrn is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-acrn":
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_virtual/kernel to linux-acrn to enable it")
}

CORE_IMAGE_EXTRA_INSTALL_append = " \
    acrn-hypervisor \
    acrn-tools \
    acrn-devicemodel \
    efibootmgr \
"

do_rootfs[depends] += "${@' '.join([i + ':do_image_complete' for i in d.getVar('ACRN_LINUX_UOS_IMAGES').split()])}"
do_rootfs[depends] += "${@['linux-acrn-uos:do_deploy', ''][d.getVar('ACRN_LINUX_UOS_IMAGES') == '']}"

bundle_guest_images () {
    install -d 0755 ${IMAGE_ROOTFS}${datadir}/acrn/images/linux
    install -d 0755 ${IMAGE_ROOTFS}${datadir}/acrn/images/android
    install -d 0755 ${IMAGE_ROOTFS}${datadir}/acrn/images/rtos

    # Hanle Linux UOS images
    cp ${DEPLOY_DIR_IMAGE}/uos-kernel/${ARCH_DEFAULT_KERNELIMAGETYPE} ${IMAGE_ROOTFS}${datadir}/acrn/images/linux
    for image_name in ${ACRN_LINUX_UOS_IMAGES}; do
        for image_type in ${ACRN_LINUX_UOS_IMAGETYPES}; do
            image=${DEPLOY_DIR_IMAGE}/$image_name-${MACHINE}.$image_type
            if [ -e $image ]; then
                cp $image ${IMAGE_ROOTFS}${datadir}/acrn/images/linux/$image_name.img
            fi
        done
    done

    # Handle Android UOS images
    # FIXME: TO BE IMPLEMENTED
    for image_name in ${ACRN_ANDROID_UOS_IMAGES}; do
        :
    done

    # Handle RTOS UOS images
    # FIXME: TO BE IMPLEMENTED
    for image_name in ${ACRN_RTOS_UOS_IMAGES}; do
        :
    done
}

IMAGE_PREPROCESS_COMMAND += "bundle_guest_images ; "

WKS_FILE = "${@bb.utils.contains_any("EFI_PROVIDER", "systemd-boot", "acrn-systemd-bootdisk-microcode.wks", "acrn-grub-bootdisk-microcode.wks", d)}"

IMAGE_FSTYPES = "wic.acrn"

inherit image-acrn
