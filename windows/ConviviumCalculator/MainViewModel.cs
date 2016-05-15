using Realms;
using System.Collections.ObjectModel;
using System.Linq;

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
            var realm = Realm.GetInstance();
            var item = realm.All<ListItem>().Where(i => i.Name == name).First();
            realm.Write(() => item.IsSwitch = isSwitch);
            realm.Close();
        }

        public MainViewModel()
        {
        }
    }
}
