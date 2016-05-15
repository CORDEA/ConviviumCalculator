using SQLite.Net.Attributes;

namespace ConviviumCalculator
{
    public class ListItem
    {
        [PrimaryKey]
        public string Name { get; set; }
        public int Price { get; set; }
        public bool IsSwitch { get; set; }
    }
}
