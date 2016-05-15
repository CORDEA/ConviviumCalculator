using System.Collections.Generic;

namespace ConviviumCalculator
{
    class DataViewModel
    {
        public void SaveCsvString(string csvString)
        {
            var conn = ConnectionUtils.GetSQLiteConnection();
            conn.DeleteAll<ListItem>();
            var csvStrings = csvString.Split('\n');
            var listItems = new List<ListItem>();

            foreach (var row in csvStrings)
            {
                var items = row.Split(',');
                if (items.Length != 3)
                {
                    continue;
                }
                var isSwitch = false;
                var price = 0;
                if (!bool.TryParse(items[2].Trim(), out isSwitch) || !int.TryParse(items[1].Trim(), out price))
                {
                    continue;
                }
                var name = items[0].Trim();
                listItems.Add(new ListItem()
                {
                    Name = items[0].Trim(),
                    Price = price,
                    IsSwitch = isSwitch
                });
            }

            conn.InsertAll(listItems);
            conn.Close();
        }

        public string CsvString
        {
            get
            {
                var conn = ConnectionUtils.GetSQLiteConnection();
                var csvString = "";

                foreach (var item in conn.Table<ListItem>())
                {
                    string row = item.Name + "," + item.Price + "," + item.IsSwitch + "\n";
                    csvString += row;
                }

                conn.Close();
                return csvString;
            }
        }
    }
}
