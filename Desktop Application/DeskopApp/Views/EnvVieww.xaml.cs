﻿using RpiApp.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Data;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Diagnostics;


namespace RpiApp.Views
{
    using Newtonsoft.Json;
    using Newtonsoft.Json.Linq;
    using RpiApp.Model;
    using RpiApp.ViewModel;
    using RpiApp.ViewModels;
    using System.Security.Cryptography.X509Certificates;

    /// <summary>
    /// Interaction logic for EnvVieww.xaml
    /// </summary>
    public partial class EnvVieww : UserControl
    {
        public EnvVieww()
        {
            InitializeComponent();
        }

    }
}
