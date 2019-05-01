FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append_class-native = " file://0001-Use-ncursesw-instead-of-ncurses.patch"
