IMAGE_INSTALL_append = " \
    acrn-hypervisor \
    acrn-tools \
    acrn-devicemodel \
"


# Variables used in acrn-sos-wks.in
ACRN_SOS_LOADER ?= "${EFI_PROVIDER}"
ACRN_I915_AVAIL_PLANES_PER_PIPE ?= "0x060000"
ACRN_I915_DOMAIN_PLANE_OWNERS ?= "0x222211111111"
ACRN_I915_DOMAIN_SCALER_OWNER ?= "0x221111"
