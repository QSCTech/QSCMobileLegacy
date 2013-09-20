jarsigner -keystore "qscmobile2\cert\keystore" -keypass "iloveqsctech" -signedjar "qscmobile2\build\apk\qscmobile2-debug-signed.apk" "qscmobile2\build\apk\qscmobile2-debug-unaligned.apk" "qscdebug"
zipalign -v 4 "qscmobile2\build\apk\qscmobile2-debug-signed.apk" "qscmobile2\build\apk\qscmobile2-debug.apk"

jarsigner -keystore "qscmobile2\cert\keystore" -keypass "iloveqsctech" -signedjar "qscmobile2\build\apk\qscmobile2-release-signed.apk" "qscmobile2\build\apk\qscmobile2-release-unsigned.apk" "release"
zipalign -v 4 "qscmobile2\build\apk\qscmobile2-release-signed.apk" "qscmobile2\build\apk\qscmobile2-release.apk"