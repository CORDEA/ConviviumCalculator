namespace ConviviumCalculator

open System

open Android.App
open Android.Content
open Android.OS
open Android.Runtime
open Android.Views
open Android.Widget

open Mono.Data.Sqlite

[<Activity (Label = "ConviviumCalculator", MainLauncher = true, Icon = "@mipmap/icon")>]
type MainActivity () =
    inherit Activity ()

    let mutable count:int = 1
    
    [<DefaultValue>] val mutable dbcon: SqliteConnection
    [<DefaultValue>] val mutable adapter: ListAdapter
    
    member me.GetItems() : Async<Model.Item list> =
        async {
            me.dbcon <- match me.dbcon with
                            | null -> new SqliteConnection(Constraints.General.connectionString)
                            | _ -> me.dbcon
                
            if me.dbcon.State.Equals Data.ConnectionState.Closed then
                me.dbcon.Open()
                
            let dbcmd = me.dbcon.CreateCommand()
            dbcmd.CommandText <- "SELECT * FROM Convivium"
            let reader = dbcmd.ExecuteReader()
            
            let list = [ while reader.Read() do yield new Model.Item(reader.GetString 0, reader.GetInt16 1, Convert.ToBoolean(reader.GetString 2)) ]
            reader.Dispose()
            me.dbcon.Close()
            return list
        }
    
    override me.OnCreate (bundle) =
        base.OnCreate (bundle)
        me.SetContentView (Resource_Layout.Main)
       
        if not (IO.File.Exists(Constraints.General.dbPath)) then
            SqliteConnection.CreateFile(Constraints.General.dbPath)
            
        let dbcon = new SqliteConnection(Constraints.General.connectionString)
        dbcon.Open()
        let dbcmd: SqliteCommand = dbcon.CreateCommand()
        dbcmd.CommandText <- "CREATE TABLE IF NOT EXISTS Convivium(name varchar(16) primary key, price int, isSwitch boolean)"
        dbcmd.ExecuteReader() |> ignore
        dbcmd.Dispose()
        dbcon.Close()
        
        let listView = me.FindViewById<ListView>(Resource_Id.list_view)
        
        me.adapter <- new ListAdapter(me, [])
        listView.Adapter <- me.adapter
        
        let onClick e =
            let intent = new Intent(me, typeof<InputDataActivity>)
            me.StartActivity intent
            
        let button = me.FindViewById<Button>(Resource_Id.button)
        
        button.Click.Add(onClick)

    override me.OnResume() =
        base.OnResume()
        me.adapter.RefreshItem(me.GetItems() |> Async.RunSynchronously)
