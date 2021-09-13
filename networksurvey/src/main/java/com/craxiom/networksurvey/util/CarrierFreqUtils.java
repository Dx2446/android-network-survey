/*
 * Copyright (C) 2016-2019 Sean J. Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.craxiom.networksurvey.util;

import com.craxiom.networksurvey.model.GnssType;

/**
 * Originally from the GPS Test open source Android app.  https://github.com/barbeau/gpstest
 */
public class CarrierFreqUtils
{
    /**
     * An unknown carrier frequency that doesn't match any known frequencies
     */
    public static final String CF_UNKNOWN = "unknown";

    /**
     * Returns the label that should be displayed for a given GNSS constellation, svid, and carrier
     * frequency in MHz, or {@link #CF_UNKNOWN} if no carrier frequency label is found
     *
     * @param gnssType            constellation type defined in GnssType
     * @param svid                identification number provided by the GnssStatus.getSvid() method
     * @param carrierFrequencyMhz carrier frequency for the signal in MHz
     * @return the label that should be displayed for a given GNSS constellation, svid, and carrier
     * frequency in MHz or {@link #CF_UNKNOWN} if no carrier frequency label is found
     */
    public static String getCarrierFrequencyLabel(GnssType gnssType, int svid, float carrierFrequencyMhz)
    {
        final float toleranceMhz = 1f;
        switch (gnssType)
        {
            case NAVSTAR:
                if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                {
                    return "L1";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1227.6f, toleranceMhz))
                {
                    return "L2";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1381.05f, toleranceMhz))
                {
                    return "L3";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1379.913f, toleranceMhz))
                {
                    return "L4";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                {
                    return "L5";
                }
                break;
            case GLONASS:
                if (carrierFrequencyMhz >= 1598.0000f && carrierFrequencyMhz <= 1610.000f)
                {
                    // Actual range is 1598.0625 MHz to 1609.3125, but allow padding for float comparisons - #103
                    return "L1";
                } else if (carrierFrequencyMhz >= 1242.0000f && carrierFrequencyMhz <= 1252.000f)
                {
                    // Actual range is 1242.9375 - 1251.6875, but allow padding for float comparisons - #103
                    return "L2";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1207.14f, toleranceMhz))
                {
                    // Exact range is unclear - appears to be 1202.025 - 1207.14 - #103
                    return "L3";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                {
                    return "L5";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                {
                    return "L1-C";
                }
                break;
            case BEIDOU:
                if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1561.098f, toleranceMhz))
                {
                    return "B1";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1589.742f, toleranceMhz))
                {
                    return "B1-2";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                {
                    return "B1C";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1207.14f, toleranceMhz))
                {
                    return "B2";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                {
                    return "B2a";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1268.52f, toleranceMhz))
                {
                    return "B3";
                }
                break;
            case QZSS:
                if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                {
                    return "L1";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1227.6f, toleranceMhz))
                {
                    return "L2";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                {
                    return "L5";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1278.75f, toleranceMhz))
                {
                    return "L6";
                }
                break;
            case GALILEO:
                if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                {
                    return "E1";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1191.795f, toleranceMhz))
                {
                    return "E5";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                {
                    return "E5a";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1207.14f, toleranceMhz))
                {
                    return "E5b";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1278.75f, toleranceMhz))
                {
                    return "E6";
                }
                break;
            case IRNSS:
                if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                {
                    return "L5";
                } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 2492.028f, toleranceMhz))
                {
                    return "S";
                }
                break;
            case SBAS:
                if (svid == 120 || svid == 123 || svid == 126 || svid == 136)
                {
                    // EGNOS - https://gssc.esa.int/navipedia/index.php/EGNOS_Space_Segment
                    if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                    {
                        return "L1";
                    } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                    {
                        return "L5";
                    }
                } else if (svid == 129 || svid == 137)
                {
                    // MSAS (Japan) - https://gssc.esa.int/navipedia/index.php/MSAS_Space_Segment
                    if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                    {
                        return "L1";
                    } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                    {
                        return "L5";
                    }
                } else if (svid == 127 || svid == 128 || svid == 139)
                {
                    // GnssType.GAGAN (India)
                    if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                    {
                        return "L1";
                    }
                } else if (svid == 133)
                {
                    // GnssType.INMARSAT_4F3;
                    if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                    {
                        return "L1";
                    } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                    {
                        return "L5";
                    }
                } else if (svid == 135)
                {
                    // GnssType.GALAXY_15;
                    if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                    {
                        return "L1";
                    } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                    {
                        return "L5";
                    }
                } else if (svid == 138)
                {
                    // GnssType.ANIK;
                    if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1575.42f, toleranceMhz))
                    {
                        return "L1";
                    } else if (MathUtils.fuzzyEquals(carrierFrequencyMhz, 1176.45f, toleranceMhz))
                    {
                        return "L5";
                    }
                }
                break;

            case UNKNOWN:
            default:
                break;
        }
        // Unknown carrier frequency for given constellation and svid
        return CF_UNKNOWN;
    }
}
