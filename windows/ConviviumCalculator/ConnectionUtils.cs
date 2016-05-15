using SQLite.Net;
using SQLite.Net.Platform.WinRT;
using System.IO;

namespace ConviviumCalculator
{
    class ConnectionUtils
    {
        private const string DB_NAME = "conv.sqlite";

        public  static SQLiteConnection GetSQLiteConnection()
        {
            var path = Path.Combine(Windows.Storage.ApplicationData.Current.LocalFolder.Path, DB_NAME);
            return new SQLiteConnection(new SQLitePlatformWinRT(), path);
        }
    }
}
