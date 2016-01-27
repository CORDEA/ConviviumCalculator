

namespace ConviviumCalculator
open System

module Constraints =
    type General() =
        static member dbPath : string =
            let dbName = "conv.db"
            let path = Environment.GetFolderPath Environment.SpecialFolder.Personal
            path + "/" + dbName
            
        static member connectionString : string =
            "Data Source=" + General.dbPath + ";Version=3"
