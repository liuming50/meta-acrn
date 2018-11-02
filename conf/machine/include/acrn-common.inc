# Use systemd for system initialization
DISTRO_FEATURES_append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = "systemd-compat-units"

PREFERRED_PROVIDER_virtual/kernel = "linux-acrn"

ACRN_LINUX_UOS_IMAGES ??= ""
ACRN_ANDROID_UOS_IMAGES ??= ""
ACRN_RTOS_UOS_IMAGES ??= ""
