meta-acrn
=========

This an OpenEmbedded/Yocto layer providing recipes for installing the ACRN hypervisor.
The layer is a rewrite of https://github.com/liuming50/meta-acrn.git by Ming Liu.

Dependencies
------------

This layer depends on:

| meta layer        | git repository                                 |
|-------------------|------------------------------------------------|
| poky              | https://git.yoctoproject.org/git/poky          |
| meta-openembedded | http://cgit.openembedded.org/meta-openembedded |
| meta-intel        | http://cgit.openembedded.org/meta-openembedded |


Usage
-----

To create an image-recipe that produces an ACRN-Service-OS image one needs
to configure local.conf as follows:

```
PREFERRED_PROVIDER_virtual/kernel = "linux-acrn"

EFI_PROVIDER = "systemd-boot"
WKS_FILE = "acrn-sos.wic-in"
```
and `inherit acrn-service-os` in said image-recipe.

Similarly when building an ACRN-User-OS set the following in your local.conf:
```
PREFERRED_PROVIDER_virtual/kernel = "linux-acrn-uos"

IMAGE_FSTYPES = "wic"
```
In this case, nothing needs to change in image-recipe you want to bitbake.

A tip is to read up on multiconfig in the Yocto manual and utilize it to
create a setup that can build and include a User-OS image into your Service-OS
image.
