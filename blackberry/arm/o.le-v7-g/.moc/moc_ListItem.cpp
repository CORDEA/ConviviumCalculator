/****************************************************************************
** Meta object code from reading C++ file 'ListItem.hpp'
**
** Created by: The Qt Meta Object Compiler version 63 (Qt 4.8.6)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../../../src/ListItem.hpp"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'ListItem.hpp' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 63
#error "This file was generated using the moc from 4.8.6. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_ListItem[] = {

 // content:
       6,       // revision
       0,       // classname
       0,    0, // classinfo
       3,   14, // methods
       3,   29, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      16,   10,    9,    9, 0x0a,
      40,   33,    9,    9, 0x0a,
      64,   54,    9,    9, 0x0a,

 // properties: name, type, flags
      87,   79, 0x0a095903,
      96,   92, 0x02095903,
     107,  102, 0x01095803,

       0        // eod
};

static const char qt_meta_stringdata_ListItem[] = {
    "ListItem\0\0nName\0setName(QString)\0"
    "nPrice\0setPrice(int)\0nIsSwitch\0"
    "isSwitch(bool)\0QString\0name\0int\0price\0"
    "bool\0isSwitch\0"
};

void ListItem::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        Q_ASSERT(staticMetaObject.cast(_o));
        ListItem *_t = static_cast<ListItem *>(_o);
        switch (_id) {
        case 0: _t->setName((*reinterpret_cast< QString(*)>(_a[1]))); break;
        case 1: _t->setPrice((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 2: _t->isSwitch((*reinterpret_cast< bool(*)>(_a[1]))); break;
        default: ;
        }
    }
}

const QMetaObjectExtraData ListItem::staticMetaObjectExtraData = {
    0,  qt_static_metacall 
};

const QMetaObject ListItem::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_ListItem,
      qt_meta_data_ListItem, &staticMetaObjectExtraData }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &ListItem::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *ListItem::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *ListItem::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_ListItem))
        return static_cast<void*>(const_cast< ListItem*>(this));
    return QObject::qt_metacast(_clname);
}

int ListItem::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 3)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 3;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QString*>(_v) = name(); break;
        case 1: *reinterpret_cast< int*>(_v) = price(); break;
        case 2: *reinterpret_cast< bool*>(_v) = isSwitch(); break;
        }
        _id -= 3;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setName(*reinterpret_cast< QString*>(_v)); break;
        case 1: setPrice(*reinterpret_cast< int*>(_v)); break;
        case 2: isSwitch(*reinterpret_cast< bool*>(_v)); break;
        }
        _id -= 3;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 3;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}
QT_END_MOC_NAMESPACE
