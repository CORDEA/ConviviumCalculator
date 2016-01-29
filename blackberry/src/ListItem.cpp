/*
 * ListItem.cpp
 *
 *  Created on: 2016/01/24
 *      Author: CORDEA
 */

#include "ListItem.hpp"

ListItem::ListItem(QObject *parent) : QObject(parent)
{
}

ListItem::ListItem(const QString name, const int price, const bool isSwitch, QObject *parent) : QObject(parent), mName(name), mPrice(price), mIsSwitch(isSwitch)
{
}

QString ListItem::name() const
{
    return mName;
}

int ListItem::price() const
{
    return mPrice;
}

bool ListItem::isSwitch() const
{
    return mIsSwitch;
}

void ListItem::setName(const QString nName)
{
    if (mName != nName) {
        mName = nName;
    }
}

void ListItem::setPrice(const int nPrice)
{
    if (mPrice != nPrice) {
        mPrice = nPrice;
    }
}

void ListItem::isSwitch(const bool nIsSwitch)
{
    if (mIsSwitch != nIsSwitch) {
        mIsSwitch = nIsSwitch;
    }
}
