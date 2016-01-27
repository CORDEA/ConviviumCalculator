

namespace ConviviumCalculator

open System
open Android.App
open Android.Widget

open Mono.Data.Sqlite
    
type ListAdapter(activity: Activity, items: Model.Item list) = 
    inherit BaseAdapter()
   
    let mutable items = items
    
    member me.RefreshItem nItems =
        items <- nItems
        me.NotifyDataSetChanged()
    
    override me.GetItem position: Java.Lang.Object =
        null
        
    override me.Count =
        items.Length
        
    override me.GetItemId position =
        int64 0
        
    override me.GetView(position, convertView, parent) : Android.Views.View =
        let view =
            match convertView with
            | null -> activity.LayoutInflater.Inflate(Resource_Layout.ListItem, parent, false)
            | _ -> convertView
        
        let title = view.FindViewById<TextView> Resource_Id.name
        let detail = view.FindViewById<TextView> Resource_Id.price
        let item_switch = view.FindViewById<Switch> Resource_Id.item_switch
        
        let item = List.nth items position
        
        title.Text <- item.name
        detail.Text <- String.Format("\u00A5 {0}", item.price)
        item_switch.Checked <- item.isSwitch
        
        let onChecked (e : CompoundButton.CheckedChangeEventArgs) =
            async {
                let dbcon = new SqliteConnection(Constraints.General.connectionString)
                dbcon.Open()
                
                let trs = dbcon.BeginTransaction()
                
                let dbcmd = dbcon.CreateCommand()
                dbcmd.CommandText <- String.Format("UPDATE Convivium SET isSwitch='{0}' WHERE name='{1}'", e.IsChecked, item.name)
                dbcmd.ExecuteReader() |> ignore
                dbcmd.Dispose()
                trs.Commit()
                
                dbcon.Close() |> ignore
            } |> Async.Start
            
        item_switch.CheckedChange.Add onChecked
        
        view
