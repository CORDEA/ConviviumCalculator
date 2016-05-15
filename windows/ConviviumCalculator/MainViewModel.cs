using SQLite.Net;
using SQLite.Net.Platform.WinRT;
using System.Collections.ObjectModel;

namespace ConviviumCalculator
{
    public class MainViewModel
    {
        private ObservableCollection<ListItem> listItems = new ObservableCollection<ListItem>();
        public ObservableCollection<ListItem> ListItems
        {
            get
            {
                return listItems;
            }
        }

        public void SaveFlag(string name, bool isSwitch)
        {
            SQLiteConnection conn = ConnectionUtils.GetSQLiteConnection();
            var item = conn.Table<ListItem>().Where(it => it.Name == name).First();
            item.IsSwitch = isSwitch;
            conn.RunInTransaction(() =>
            {
                conn.Update(item);
            });
            conn.Close();
        }

        private void GetItemsFromDb(SQLiteConnection conn)
        {
            var items = conn.Table<ListItem>();
            foreach(var item in items)
            {
                listItems.Add(item);
            }
        }

        public MainViewModel()
        {
            var conn = ConnectionUtils.GetSQLiteConnection();
            conn.CreateTable<ListItem>();
            GetItemsFromDb(conn);
            conn.Close();
        }
    }
}
