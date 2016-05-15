using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConviviumCalculator
{
    public class ListItem
    {
        public string Name { get; set; }
        public int Price { get; set; }
        public bool IsSwitch { get; set; }

        public ListItem(string name, int price, bool isSwitch)
        {
            Name = name;
            Price = price;
            IsSwitch = isSwitch;
        }
    }
}
