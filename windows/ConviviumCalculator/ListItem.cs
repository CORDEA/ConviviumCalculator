using Realms;

namespace ConviviumCalculator
{
    public class ListItem : RealmObject
    {
        public string Name { get; set; }
        public int Price { get; set; }
        public bool IsSwitch { get; set; }
    }
}
