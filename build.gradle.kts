allprojects {
    apply(plugin = "idea")

    version = "1.0"

    extra["appName"] = "Spitfire"
    extra["gdxVersion"] = "1.12.1"
    extra["roboVMVersion"] = "2.3.20"
    extra["gamesvcsVersion"] = "1.1.0"
    extra["visuiVersion"] = "1.5.3"
}
