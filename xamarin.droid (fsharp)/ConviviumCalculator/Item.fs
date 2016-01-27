namespace ConviviumCalculator

open System
open Android

module Model =
    type Item(name, price, isSwitch) =
        member me.name: string = name
        member me.price: int16 = price
        member me.isSwitch: bool = isSwitch
