ACRN_PLATFORM ??= "uefi"

ACRN_UEFI_ARCH ?= "${@['ia32', 'x64'][d.getVar('TARGET_ARCH') == 'x86_64']}"
ACRN_UEFI_IMAGE_NAME ?= "boot${ACRN_UEFI_ARCH}"
ACRN_UEFI_OS_LOADER_DIR ?= "${@['GRUB', 'SYSTEMD'][d.getVar('EFI_PROVIDER') == 'systemd-boot']}"
ACRN_UEFI_OS_LOADER_NAME ?= "${@['grub-efi', 'systemd'][d.getVar('EFI_PROVIDER') == 'systemd-boot']}-${ACRN_UEFI_IMAGE_NAME}"
