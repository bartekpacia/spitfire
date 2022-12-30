allprojects {
    apply(plugin = "idea")

    version = "1.0"

    extra["appName"] = "Spitfire"
    extra["gdxVersion"] = "1.10.0"
    extra["roboVMVersion"] = "2.3.15"
    extra["gamesvcsVersion"] = "1.1.0"
    extra["visuiVersion"] = "1.3.0"
}
