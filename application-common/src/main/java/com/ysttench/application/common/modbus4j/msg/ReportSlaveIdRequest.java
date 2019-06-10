/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ysttench.application.common.modbus4j.msg;

import com.ysttench.application.common.modbus4j.Modbus;
import com.ysttench.application.common.modbus4j.ProcessImage;
import com.ysttench.application.common.modbus4j.code.FunctionCode;
import com.ysttench.application.common.modbus4j.exception.ModbusTransportException;
import com.ysttench.application.common.modbus4j.sero.util.queue.ByteQueue;

public class ReportSlaveIdRequest extends ModbusRequest {
    public ReportSlaveIdRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    @Override
    public void validate(Modbus modbus) {
        // no op
    }

    @Override
    protected void writeRequest(ByteQueue queue) {
        // no op
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        // no op
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReportSlaveIdResponse(slaveId);
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReportSlaveIdResponse(slaveId, processImage.getReportSlaveIdData());
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.REPORT_SLAVE_ID;
    }
}
