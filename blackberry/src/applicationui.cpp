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

#include "applicationui.hpp"

#include <QDir>
#include <QFile>
#include <QVariant>
#include <QVariantList>

#include <bb/cascades/Application>
#include <bb/cascades/QmlDocument>
#include <bb/cascades/LocaleHandler>
#include <bb/cascades/ListView>
#include <bb/cascades/GroupDataModel>

using namespace bb::data;

#include "ListItem.hpp"

using namespace bb::cascades;

ApplicationUI::ApplicationUI() :
        QObject()
{
    // prepare the localization
    m_pTranslator = new QTranslator(this);
    m_pLocaleHandler = new LocaleHandler(this);

    bool res = QObject::connect(m_pLocaleHandler, SIGNAL(systemLanguageChanged()), this, SLOT(onSystemLanguageChanged()));
    // This is only available in Debug builds
    Q_ASSERT(res);
    // Since the variable is not used in the app, this is added to avoid a
    // compiler warning
    Q_UNUSED(res);

    // initial load
    onSystemLanguageChanged();

    // Create scene document from main.qml asset, the parent is set
    // to ensure the document gets destroyed properly at shut down.
    QmlDocument *qml = QmlDocument::create("asset:///main.qml").parent(this);
    qml->setContextProperty("app", this);

    // Create root object for the UI
    root = qml->createRootObject<AbstractPane>();

    // Set created root object as the application scene

    Application::instance()->setScene(root);

    sda = NULL;

    createTable();

    initializeListAsync();
}

void ApplicationUI::initializeList()
{
    QFile file(QDir::home().absoluteFilePath(dbName()));
    if (file.open(QIODevice::ReadOnly)) {
        checkSqlDataAccess();
        QVariant list = sda->execute("SELECT * FROM Convivium");
        GroupDataModel *dataModel = root->findChild<GroupDataModel *>("ConviviumData");

        dataModel->insertList(list.value<QVariantList>());
    }
}

void ApplicationUI::initializeListAsync()
{
    QtConcurrent::run(this, &ApplicationUI::initializeList);
}

QString ApplicationUI::dbName() const
{
    return "conv.db";
}

void ApplicationUI::checkSqlDataAccess()
{
    if (sda == NULL)
    {
        sda = new SqlDataAccess(QDir::home().absoluteFilePath(dbName()), this);
    }
}

void ApplicationUI::onSwitch(QString name, bool isSwitch)
{
    QFile file(QDir::home().absoluteFilePath(dbName()));
    if (file.open(QIODevice::ReadWrite)) {
        checkSqlDataAccess();
        sda->execute(QString("UPDATE Convivium SET isSwitch='%1' WHERE name='%2'").arg(isSwitch).arg(name));
    }
}

void ApplicationUI::createTable()
{
    QFile file(QDir::home().absoluteFilePath(dbName()));
    if (file.open(QIODevice::ReadWrite)) {
        checkSqlDataAccess();
        sda->execute("CREATE TABLE IF NOT EXISTS Convivium(name varchar(16) primary key, price int, isSwitch boolean)");
    }
}

QVariant ApplicationUI::getCsvString()
{
    QString str = "";
    QFile file(QDir::home().absoluteFilePath(dbName()));
    if (file.open(QIODevice::ReadOnly))
    {
        checkSqlDataAccess();
        QVariantList v = sda->execute("SELECT * FROM Convivium").value<QVariantList>();
        for (int i = 0; i < v.length(); i++)
        {
            QVariantMap map = v[i].value<QVariantMap>();
            str += map["name"].value<QString>() + "," +
                   map["price"].value<QString>() + "," +
                   (map["isSwitch"].value<int>() == 1 ? "true" : "false") + "\n";
        }
    }
    return str;
}

QVariant ApplicationUI::getCsvStringSeparate()
{
    QFuture<QVariant> future = QtConcurrent::run(this, &ApplicationUI::getCsvString);
    return future.result();
}

void ApplicationUI::readCsvObject(const QVariantList list)
{
    QFile file(QDir::home().absoluteFilePath(dbName()));
    if (file.open(QIODevice::ReadWrite))
    {
        checkSqlDataAccess();
        sda->execute("DELETE FROM Convivium");
        for (int i = 0; i < list.length(); i++)
        {
            QVariantList v = list[i].value<QVariantList>();
            if (3 == v.size()) {
                sda->execute(QString("INSERT INTO Convivium (name, price, isSwitch) VALUES ('%1', %2, '%3')").arg(v[0].toString()).arg(v[1].toString()).arg(v[2].toString()));
            }
        }

        QVariant list = sda->execute("SELECT * FROM Convivium");
        GroupDataModel *dataModel = root->findChild<GroupDataModel *>("ConviviumData");

        dataModel->clear();
        dataModel->insertList(list.value<QVariantList>());
    }
}

void ApplicationUI::readCsvObjectSeparate(const QVariantList list)
{
    QtConcurrent::run(this, &ApplicationUI::readCsvObject, list).waitForFinished();
}

void ApplicationUI::onSystemLanguageChanged()
{
    QCoreApplication::instance()->removeTranslator(m_pTranslator);
    // Initiate, load and install the application translation files.
    QString locale_string = QLocale().name();
    QString file_name = QString("ConviviumCalculator_%1").arg(locale_string);
    if (m_pTranslator->load(file_name, "app/native/qm")) {
        QCoreApplication::instance()->installTranslator(m_pTranslator);
    }
}
