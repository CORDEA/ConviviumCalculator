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

#ifndef ApplicationUI_HPP_
#define ApplicationUI_HPP_

#include <QObject>
#include <QFile>

#include <bb/data/SqlDataAccess>
#include <bb/cascades/AbstractPane>

namespace bb
{
    namespace cascades
    {
        class LocaleHandler;
    }
}

class QTranslator;

/*!
 * @brief Application UI object
 *
 * Use this object to create and init app UI, to create context objects, to register the new meta types etc.
 */
class ApplicationUI : public QObject
{
    Q_OBJECT
public:
    ApplicationUI();

    Q_INVOKABLE void onSwitch(QString name, bool isSwitch);
    Q_INVOKABLE void readCsvObjectSeparate(QVariantList var);
    Q_INVOKABLE QVariant getCsvStringSeparate();

    virtual ~ApplicationUI() {}
private slots:
    void onSystemLanguageChanged();
private:
    bb::data::SqlDataAccess *sda;
    QString dbName() const;

    QTranslator *m_pTranslator;
    bb::cascades::LocaleHandler *m_pLocaleHandler;

    bb::cascades::AbstractPane *root;

    void initializeList();
    void initializeListAsync();

    void createTable();
    QFile getDbFile();
    void checkSqlDataAccess();
    void readCsvObject(QVariantList var);
    QVariant getCsvString();
};

#endif /* ApplicationUI_HPP_ */
