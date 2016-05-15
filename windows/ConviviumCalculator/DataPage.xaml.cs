using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=234238

namespace ConviviumCalculator
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class DataPage : Page
    {
        private DataViewModel ViewModel { get; set; }

        public DataPage()
        {
            this.InitializeComponent();
            ViewModel = new DataViewModel();
        }

        private void AppBarAcceptButton_Click(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrEmpty(textBox.Text))
            {
                return;
            }
            ViewModel.SaveCsvString(textBox.Text);
            Frame.GoBack();
        }

        private void AppBarCancelButton_Click(object sender, RoutedEventArgs e)
        {
            Frame.GoBack();
        }
    }
}
