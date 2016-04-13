//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

import java.util.List;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;

/**
 * Base class where class is derived.
 */

public class GXDLMSSchedule extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Specifies the scripts to be executed at given times.
     */
    private List<GXDLMSScheduleEntry> entries;

    /**
     * Constructor.
     */
    public GXDLMSSchedule() {
        super(ObjectType.SCHEDULE);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSchedule(final String ln) {
        super(ObjectType.SCHEDULE, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSSchedule(final String ln, final int sn) {
        super(ObjectType.SCHEDULE, ln, sn);
    }

    /**
     * Specifies the scripts to be executed at given times.
     * 
     * @return List of executed schedule entries.
     */
    public final List<GXDLMSScheduleEntry> getEntries() {
        return entries;
    }

    /**
     * Specifies the scripts to be executed at given times.
     * 
     * @param value
     *            List of executed schedule entries.
     */
    public final void setEntries(final List<GXDLMSScheduleEntry> value) {
        entries = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), entries };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // ExecutedScriptLogicalName is static and read only once.
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // Type is static and read only once.
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // ExecutionTime is static and read only once.
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 2;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 3;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        // TODO:
        throw new IllegalArgumentException(
                "GetValue failed. Invalid attribute index.");
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final int index,
            final Object value) {
        if (index == 1) {
            super.setValue(settings, index, value);
        } else if (index == 2) {
            entries.clear();
            Object[] arr = (Object[]) value;
            for (Object it : arr) {
                GXDLMSScheduleEntry item = new GXDLMSScheduleEntry();
                Object[] tmp = (Object[]) it;
                item.setIndex(((Number) tmp[0]).byteValue());
                item.setEnable(((Boolean) tmp[1]).booleanValue());
                item.setLogicalName(GXDLMSClient
                        .changeType((byte[]) tmp[2], DataType.OCTET_STRING)
                        .toString());
                item.setScriptSelector(((Number) tmp[3]).byteValue());
                item.setSwitchTime((GXDateTime) GXDLMSClient
                        .changeType((byte[]) tmp[4], DataType.DATETIME));
                item.setValidityWindow(((Number) tmp[5]).byteValue());
                item.setExecWeekdays((String) tmp[6]);
                item.setExecSpecDays((String) tmp[7]);
                item.setBeginDate((GXDateTime) GXDLMSClient
                        .changeType((byte[]) tmp[8], DataType.DATETIME));
                item.setEndDate((GXDateTime) GXDLMSClient
                        .changeType((byte[]) tmp[9], DataType.DATETIME));
                entries.add(item);
            }
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}