﻿using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace ConviviumCalculator
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        public MainPage()
        {
            this.InitializeComponent();
            ViewModel = new MainViewModel();
        }
        
        public MainViewModel ViewModel { get; set; }

        private void CheckBox_CheckedChanged(object sender, RoutedEventArgs e)
        {
            var checkBox = (CheckBox)sender;
            var context = checkBox.DataContext;
            if (context is ListItem)
            {
                var item = context as ListItem;
                ViewModel.SaveFlag(item.Name, checkBox.IsChecked ?? false);
            }
        }
    }
}
