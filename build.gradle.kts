allprojects {
    apply(plugin = "idea")

    version = "1.0"

    extra["appName"] = "Spitfire"
    extra["gdxVersion"] = "1.12.0"
    extra["roboVMVersion"] = "2.3.19"
    extra["gamesvcsVersion"] = "1.1.0"
    extra["visuiVersion"] = "1.5.2"
}
