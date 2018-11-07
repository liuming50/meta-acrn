IMAGE_TYPES_append = " wic.acrn"
CONVERSIONTYPES_append = " acrn"

mkimage_uefi_acrn () {
    # Remove the original efi image
    wic rm \
        ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic:1/EFI/BOOT/${ACRN_UEFI_IMAGE_NAME}.efi \
        --native-sysroot ${STAGING_DIR_NATIVE}

    # Add acrn.efi as the default efi image
    wic cp \
        ${DEPLOY_DIR_IMAGE}/acrn-${ACRN_UEFI_IMAGE_NAME}.efi \
        ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic:1/EFI/BOOT/${ACRN_UEFI_IMAGE_NAME}.efi \
        --native-sysroot ${STAGING_DIR_NATIVE}

    # Install the efi os loader
    mkdir -p ${WORKDIR}/${ACRN_UEFI_OS_LOADER_DIR}
    cp ${DEPLOY_DIR_IMAGE}/${ACRN_UEFI_OS_LOADER_NAME}.efi ${WORKDIR}/${ACRN_UEFI_OS_LOADER_DIR}
    wic cp \
        ${WORKDIR}/${ACRN_UEFI_OS_LOADER_DIR} ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic:1/EFI \
        --native-sysroot ${STAGING_DIR_NATIVE}

    cp ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic.acrn
    rm -rf ${WORKDIR}/${ACRN_UEFI_OS_LOADER_DIR}
}

CONVERSION_CMD_acrn () {
    if [ "${ACRN_PLATFORM}" = "uefi" ]; then
        mkimage_uefi_acrn
    fi
}

do_image_wic[depends] += "${MLPREFIX}acrn-hypervisor:do_deploy"
