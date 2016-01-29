import bb.cascades 1.4
import bb.system 1.0

Page {
    Container {
        horizontalAlignment: HorizontalAlignment.Fill
        TextArea {
            id: textArea
            preferredHeight: maxHeight
        }
    }
    actions: [
        ActionItem {
            title: "input"
            ActionBar.placement: ActionBarPlacement.OnBar
            onTriggered: {
                var arr = []
                if (csvParser(arr)) {
                    nav.popWithParam(arr)
                }
            }
            attachedObjects: [
                SystemToast {
                    id: toast
                    body: "Failed to csv parsing"
                }
            ]
            
            function csvParser(arr) {
                var lines = textArea.text.split("\n")
                for (var i = 0; i < lines.length; i++) {
                    if (!lines[i]) {
                        continue
                    }
                    var inLines = lines[i].split(",")
                    
                    if (inLines.length === 3 && parseInt(inLines[1], 10) !== undefined &&
                        (inLines[2] === "true" || inLines[2] === "false")) {
                        arr.push(inLines)
                    } else {
                        toast.show()
                        return false
                    }
                    arr.push(inLines)
                }
                return true
            }
        }
    ]
    onCreationCompleted: {
        textArea.text = app.getCsvStringSeparate()
    }
}
