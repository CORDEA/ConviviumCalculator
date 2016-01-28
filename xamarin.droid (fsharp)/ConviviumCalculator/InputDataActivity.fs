
namespace ConviviumCalculator

open System
open System.Collections.Generic
open System.Linq
open System.Text

open Android.App
open Android.Content
open Android.OS
open Android.Runtime
open Android.Views
open Android.Widget
open Android.Util
open Android.Support.V7.Widget
open Android.Support.V7.App

open Mono.Data.Sqlite

[<Activity (Label = "InputData", Theme = "@style/AppTheme.NoActionBar")>]
type InputDataActivity() =
  inherit AppCompatActivity()
  
  [<DefaultValue>] val mutable editText: EditText
  [<DefaultValue>] val mutable dbcon: SqliteConnection
  
  member me.InsertData(text: string) =
      async {
          let dbcmd = me.dbcon.CreateCommand()
          let cmd str =
              dbcmd.CommandText <- str
              dbcmd.ExecuteReader() |> ignore
              dbcmd.Dispose()
              
          let trs = me.dbcon.BeginTransaction()
          text.Split([|"\n"|], System.StringSplitOptions.None)
                |> Array.toList
                |> List.map(fun k -> k.Split([|","|], System.StringSplitOptions.None) |> Array.toList)
                |> List.filter(fun k -> k.Length.Equals 3)
                |> List.map(fun k -> new Model.Item(List.nth k 0, int16(List.nth k 1), Convert.ToBoolean(List.nth k 2)))
                |> List.map(fun k -> String.Format("INSERT INTO Convivium (name, price, isSwitch) VALUES ('{0}', {1}, '{2}')", k.name, k.price, k.isSwitch))
                |> List.iter(fun k -> cmd(k))
          trs.Commit()
      }
  
  override me.OnCreate(bundle) =
    base.OnCreate (bundle)
    me.SetContentView (Resource_Layout.InputData)
     
    let toolbar = me.FindViewById<Toolbar>(Resource_Id.toolbar)
    me.SetSupportActionBar toolbar
       
    me.editText <- me.FindViewById<EditText> Resource_Id.edit_text

    me.dbcon <- new SqliteConnection(Constraints.General.connectionString)
    me.dbcon.Open()
    let dbcmd = me.dbcon.CreateCommand()
    dbcmd.CommandText <- "SELECT * FROM Convivium"
    let reader = dbcmd.ExecuteReader()
    me.editText.Text <- [ while reader.Read() do yield String.Format("{0},{1},{2}\n", reader.GetString 0, reader.GetInt16 1, reader.GetString 2) ]
                            |> String.Concat
    dbcmd.Dispose()
        
    let trs = me.dbcon.BeginTransaction()
    let dbcmd = me.dbcon.CreateCommand()
    dbcmd.CommandText <- "DELETE FROM Convivium"
    dbcmd.ExecuteReader() |> ignore
    dbcmd.Dispose()
    trs.Commit()
    
  override me.OnPause() =
      base.OnPause()
      let text: string = me.editText.Text.ToString()
         
      me.InsertData(text) |> Async.RunSynchronously
