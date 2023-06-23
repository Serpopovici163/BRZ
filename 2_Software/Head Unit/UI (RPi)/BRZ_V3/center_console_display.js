function changeCenterConsoleTab(tab) {

    document.getElementById("convenience-tab").classList.remove("tab-selected");
    document.getElementById("diagnostic-tab").classList.remove("tab-selected");
    document.getElementById("settings-tab").classList.remove("tab-selected");

    if (tab == 0) {
        //convenience tab
        document.getElementById("convenience-tab").classList.add("tab-selected");
    } else if (tab == 1) {
        //diagnostic tab (default)
        document.getElementById("diagnostic-tab").classList.add("tab-selected");
    } else {
        //settings tab
        document.getElementById("settings-tab").classList.add("tab-selected");
    }
}