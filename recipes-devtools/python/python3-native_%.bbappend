FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append = " file://0001-Use-ncursesw-instead-of-ncurses.patch"
