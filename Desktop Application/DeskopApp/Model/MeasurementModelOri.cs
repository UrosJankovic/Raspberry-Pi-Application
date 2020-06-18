﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpiApp.Model
{
    public class MeasurementModelOri
    {
        public string Name { get; set; }
        public DataOri Data { get; set; }
        public string Unit { get; set; }
        
    }
    public class DataOri
    {
        public double Roll { get; set; }
        public double Pitch { get; set; }
        public double Yaw { get; set; }

        public double x { get; set; }
        public double y { get; set; }
        public double z { get; set; }
    }


}
