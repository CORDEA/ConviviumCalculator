/*
 * ListItem.hpp
 *
 *  Created on: 2016/01/24
 *      Author: CORDEA
 */

#ifndef LISTITEM_HPP_
#define LISTITEM_HPP_

#include <QObject>

class ListItem : public QObject
{
    Q_OBJECT;

    Q_PROPERTY(QString name READ name WRITE setName FINAL);
    Q_PROPERTY(int price READ price WRITE setPrice FINAL);
    Q_PROPERTY(bool isSwitch READ isSwitch WRITE isSwitch FINAL)

    public:
        ListItem(QObject *parent = 0);
        ListItem(QString name, int price, bool isSwitch, QObject *parent = 0);

        QString name() const;
        int price() const;
        bool isSwitch() const;

public Q_SLOTS:
    void setName(QString nName);
    void setPrice(int nPrice);
    void isSwitch(bool nIsSwitch);

private:
    QString mName;
    int mPrice;
    bool mIsSwitch;
};

#endif /* LISTITEM_HPP_ */
