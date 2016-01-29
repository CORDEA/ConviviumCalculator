/*
 * Copyright (c) 2011-2015 BlackBerry Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import bb.cascades 1.4
import listItem 1.0

NavigationPane {
    id: nav
    Page {
        Container {
            attachedObjects: [
                GroupDataModel {
                    id: dataModel
                    objectName: "ConviviumData"
                    sortingKeys: ["name"] 
                }
            ]
            ListView {
                id: listView
                dataModel: dataModel
                listItemComponents: [
                    ListItemComponent {
                        type: "item"
                        Container {
                            verticalAlignment:VerticalAlignment.Center
                            rightPadding: 20
                            leftPadding: 20
                            layout: DockLayout {
                            
                            }
                            Container {
                                verticalAlignment: VerticalAlignment.Center
                                horizontalAlignment: HorizontalAlignment.Fill
                                rightPadding: 150
                                layout: StackLayout {
                                    orientation: LayoutOrientation.TopToBottom
                                }
                                Label {
                                    text: ListItemData.name
                                    textStyle.fontSize: FontSize.Large
                                    id: title
                                }
                                Label {
                                    horizontalAlignment: HorizontalAlignment.Right
                                    text: ListItemData.price.toString()
                                    id: desc
                                }
                            }
                            ToggleButton {
                                verticalAlignment: VerticalAlignment.Center
                                horizontalAlignment: HorizontalAlignment.Right
                                checked: ListItemData.isSwitch
                                
                                onCheckedChanged: {
                                    Qt.app.onSwitch(title.text, checked);
                                }
                            }
                            Divider {
                            
                            }
                        }
                    }
                ]
            }
        }
        actions: [
            ActionItem {
                title: "add data"
                ActionBar.placement: ActionBarPlacement.OnBar
                onTriggered: {
                    nav.push(pageDef.createObject())
                }
                attachedObjects: ComponentDefinition {
                    id: pageDef
                    source: "input_data.qml"
                }
            }
        ]

    }
    onPopTransitionEnded: {
        page.destroy();
    }
    function popWithParam(param) {
        app.readCsvObjectSeparate(param)
        pop()
    }
    onCreationCompleted: {
        Qt.app = app
    }
}
